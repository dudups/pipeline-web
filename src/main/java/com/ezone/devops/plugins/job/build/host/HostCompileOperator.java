package com.ezone.devops.plugins.job.build.host;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.enums.ResourceType;
import com.ezone.devops.pipeline.exception.CommonException;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.util.FreemarkerUtil;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.enums.ArtifactType;
import com.ezone.devops.plugins.job.AbstractPluginOperator;
import com.ezone.devops.plugins.job.PluginOperator;
import com.ezone.devops.plugins.job.build.host.model.HostCompileBuild;
import com.ezone.devops.plugins.job.build.host.model.HostCompileConfig;
import com.ezone.devops.plugins.job.build.host.service.HostCompileBuildService;
import com.ezone.devops.plugins.job.enums.VersionType;
import com.ezone.devops.plugins.service.ArtifactInfoService;
import com.ezone.devops.scheduler.client.response.RunnerJobInfo;
import com.ezone.devops.scheduler.enums.ExecutorType;
import com.ezone.devops.scheduler.job.CommonJob;
import com.ezone.devops.scheduler.job.FileUploadStep;
import com.ezone.devops.scheduler.job.HostSteps;
import com.ezone.devops.scheduler.job.ReportUploadStep;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class HostCompileOperator extends AbstractPluginOperator implements PluginOperator {

    @Autowired
    private HostCompileDataOperator hostCompileDataOperator;
    @Autowired
    private HostCompileBuildService hostCompileBuildService;
    @Autowired
    private ArtifactInfoService artifactInfoService;

    private String getClusterName(Pipeline pipeline, HostCompileConfig hostCompileConfig) {
        String clusterName;
        if (hostCompileConfig.isUseSelfCiPool()) {
            clusterName = hostCompileConfig.getClusterName();
        } else {
            if (pipeline.getResourceType() != ResourceType.HOST) {
                throw new CommonException("此插件仅支持主机集群，请检查流水线配置");
            }
            clusterName = pipeline.getClusterName();
        }

        return clusterName;
    }

    @Override
    public boolean execute(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        HostCompileConfig hostCompileConfig = hostCompileDataOperator.getRealJob(jobRecord.getPluginId());

        String clusterName = getClusterName(pipeline, hostCompileConfig);
        Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);
        HostCompileBuild hostCompileBuild = hostCompileDataOperator.getRealJobRecord(jobRecord.getPluginRecordId());

        boolean uploadArtifact = hostCompileConfig.isUploadArtifact();
        FileUploadStep fileUploadStep = null;
        if (uploadArtifact) {
            String version = pipelineRecord.getSnapshotVersion();
            if (hostCompileConfig.getVersionType() == VersionType.CUSTOM_VERSION) {
                version = FreemarkerUtil.parseExpression(hostCompileConfig.getCustomVersion(), envs);
                if (StringUtils.isBlank(version)) {
                    super.noticeJobFailed(pipelineRecord, jobRecord, "解析version出错");
                    return false;
                }
            } else if (hostCompileConfig.getVersionType() == VersionType.RELEASE) {
                version = pipelineRecord.getReleaseVersion();
                if (StringUtils.isBlank(version)) {
                    super.noticeJobFailed(pipelineRecord, jobRecord, "流水线尚未发版，无法使用发布版本");
                    return false;
                }
            }

            fileUploadStep = new FileUploadStep(hostCompileConfig, version);
        }

        ReportUploadStep reportUploadStep = null;
        if (hostCompileConfig.isUploadReport()) {
            String reportId = createReportInfo(pipeline);
            hostCompileBuild.setReportId(reportId);
            hostCompileBuildService.updateBuild(hostCompileBuild);
            reportUploadStep = new ReportUploadStep().setUploadReport(true).setReportId(reportId).setReportDir(hostCompileConfig.getReportDir()).setReportIndex(hostCompileConfig.getReportIndex());
        }

        HostSteps hostSteps = new HostSteps(fileUploadStep, reportUploadStep);
        CommonJob<HostSteps> commonJob = new CommonJob<HostSteps>().setCloneMode(hostCompileConfig.getCloneMode()).setJobType(jobRecord.getJobType()).setCommand(hostCompileConfig.getBuildCommand()).setSteps(hostSteps);

        RunnerJobInfo runnerJobInfo = createRunnerJob(repo, pipeline, jobRecord, ResourceType.HOST, clusterName, ExecutorType.MACHINE, null, envs, commonJob);

        if (runnerJobInfo.isSuccess()) {
            jobRecord.setTaskId(runnerJobInfo.getTaskId());
            jobRecord.setLogName(runnerJobInfo.getLogName());
        } else {
            super.noticeJobFailed(pipelineRecord, jobRecord, runnerJobInfo.getMessage());
        }

        return true;
    }

    @Override
    public void cancel(PipelineRecord pipelineRecord, JobRecord jobRecord, String triggerUser) {
        sendCancelJobLog(jobRecord, triggerUser);
        schedulerClient.cancelRunnerJob(jobRecord.getTaskId());
    }

    @Override
    public void jobStartCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message) {
    }

    @Override
    public void jobSuccessCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data) {
        HostCompileConfig hostCompileConfig = hostCompileDataOperator.getRealJob(jobRecord.getPluginId());
        if (hostCompileConfig.isUploadArtifact()) {
            String version = pipelineRecord.getSnapshotVersion();
            if (hostCompileConfig.getVersionType() == VersionType.CUSTOM_VERSION) {
                RepoVo repo = repoService.getByRepoKey(pipelineRecord.getCompanyId(), pipelineRecord.getRepoKey());
                Pipeline pipeline = pipelineService.getByIdIfPresent(pipelineRecord.getPipelineId());
                Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);
                version = FreemarkerUtil.parseExpression(hostCompileConfig.getCustomVersion(), envs);
            } else if (hostCompileConfig.getVersionType() == VersionType.RELEASE) {
                version = pipelineRecord.getReleaseVersion();
            }

            artifactInfoService.saveArtifactInfo(pipelineRecord, getArtifactType(jobRecord), hostCompileConfig.getPkgRepo(), hostCompileConfig.getPkgName(), version);
        }

        if (hostCompileConfig.isUploadReport()) {
            HostCompileBuild hostCompileBuild = hostCompileBuildService.getById(jobRecord.getPluginRecordId());
            String reportDashBoardUrl = getReportDashBoardUrl(hostCompileConfig, hostCompileBuild);
            hostCompileBuild.setReportDashboardUrl(reportDashBoardUrl);
            hostCompileBuildService.updateBuild(hostCompileBuild);
        }
        super.jobSuccessCallback(pipelineRecord, jobRecord, message, data);
    }

    private String getReportDashBoardUrl(HostCompileConfig hostCompileConfig, HostCompileBuild hostCompileBuild) {
        return String.format(REPORT_DASH_BOARD_URL, hostCompileBuild.getReportId(), hostCompileConfig.getReportIndex());
    }

    private ArtifactType getArtifactType(JobRecord jobRecord) {
        String jobType = jobRecord.getJobType();
        if (jobType.equals(AndroidHostCompilePluginInfo.JOB_TYPE)) {
            return ArtifactType.APK;
        } else if (jobType.equals(IOSHostCompilePluginInfo.JOB_TYPE)) {
            return ArtifactType.IPA;
        } else {
            return ArtifactType.RAW;
        }
    }

    @Override
    public void jobAbortCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data) {
        super.jobAbortCallback(pipelineRecord, jobRecord, message, data);
    }
}