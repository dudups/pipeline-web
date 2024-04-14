package com.ezone.devops.plugins.job.build.helm;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.util.FreemarkerUtil;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.job.AbstractPluginOperator;
import com.ezone.devops.plugins.job.PluginOperator;
import com.ezone.devops.plugins.job.build.helm.model.HelmPackageConfig;
import com.ezone.devops.plugins.job.enums.VersionType;
import com.ezone.devops.scheduler.bean.ImageInfo;
import com.ezone.devops.scheduler.client.response.RunnerJobInfo;
import com.ezone.devops.scheduler.enums.ExecutorType;
import com.ezone.devops.scheduler.job.CommonJob;
import com.ezone.devops.scheduler.job.HelmPackageStep;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class HelmPackageOperator extends AbstractPluginOperator implements PluginOperator {

    @Autowired
    private HelmPackageDataOperator helmPackageDataOperator;

    @Override
    public boolean execute(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        HelmPackageConfig helmPackageConfig = helmPackageDataOperator.getRealJob(jobRecord.getPluginId());

        Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);

        String image = getBuildImageUrl(helmPackageConfig.getBuildImageId());

        HelmPackageStep helmPackageStep = new HelmPackageStep();
        helmPackageStep.setPkgRepoType(helmPackageConfig.getPkgRepoType());
        helmPackageStep.setPkgRepoName(helmPackageConfig.getPkgRepoName());
        helmPackageStep.setChartResourcePath(helmPackageConfig.getChartResourcePath());

        String chartVersion = null;
        if (helmPackageConfig.getChartVersionType() == VersionType.DEFAULT_VERSION) {
            helmPackageStep.setUseDefaultVersion(true);
        } else if (helmPackageConfig.getChartVersionType() == VersionType.CUSTOM_VERSION) {
            String customVersion = FreemarkerUtil.parseExpression(helmPackageConfig.getCustomVersion(), envs);
            if (StringUtils.isBlank(customVersion)) {
                super.noticeJobFailed(pipelineRecord, jobRecord, "解析version出错");
                return false;
            }
            chartVersion = customVersion.replaceAll("_", "-");
        } else if (helmPackageConfig.getChartVersionType() == VersionType.SNAPSHOT) {
            chartVersion = pipelineRecord.getSnapshotVersion().replaceAll("_", "-");
        } else {
            String releaseVersion = pipelineRecord.getReleaseVersion();
            if (StringUtils.isBlank(releaseVersion)) {
                super.noticeJobFailed(pipelineRecord, jobRecord, "流水线尚未发版，无法使用发布版本");
                return false;
            }
            chartVersion = releaseVersion.replaceAll("_", "-");
        }
        helmPackageStep.setCustomVersion(chartVersion);

        String appVersion = null;
        if (helmPackageConfig.getAppVersionType() == VersionType.DEFAULT_VERSION) {
            helmPackageStep.setUseDefaultAppVersion(true);
        } else if (helmPackageConfig.getAppVersionType() == VersionType.CUSTOM_VERSION) {
            appVersion = FreemarkerUtil.parseExpression(helmPackageConfig.getAppCustomVersion(), envs);
            if (StringUtils.isBlank(appVersion)) {
                super.noticeJobFailed(pipelineRecord, jobRecord, "解析app-version出错");
                return false;
            }
            appVersion = appVersion.replaceAll("_", "-");
        } else if (helmPackageConfig.getAppVersionType() == VersionType.SNAPSHOT) {
            appVersion = pipelineRecord.getSnapshotVersion().replaceAll("_", "-");
        } else {
            String releaseVersion = pipelineRecord.getReleaseVersion();
            if (StringUtils.isBlank(releaseVersion)) {
                super.noticeJobFailed(pipelineRecord, jobRecord, "流水线尚未发版，无法使用发布版本");
                return false;
            }
            appVersion = releaseVersion.replaceAll("_", "-");
        }

        helmPackageStep.setAppCustomVersion(appVersion);

        if (helmPackageConfig.isUseDefaultName()) {
            helmPackageStep.setUseDefaultName(true);
        } else {
            String chartName = FreemarkerUtil.parseExpression(helmPackageConfig.getChartName(), envs);
            if (StringUtils.isBlank(chartName)) {
                super.noticeJobFailed(pipelineRecord, jobRecord, "解析chart名称出错");
                return false;
            }
            helmPackageStep.setUseDefaultName(false);
            helmPackageStep.setChartName(chartName);
        }

        CommonJob<HelmPackageStep> commonJob = new CommonJob<HelmPackageStep>()
                .setCloneMode(helmPackageConfig.getCloneMode())
                .setJobType(jobRecord.getJobType())
                .setCommand(helmPackageConfig.getCommand())
                .setSteps(helmPackageStep);

        ImageInfo buildImageInfo = new ImageInfo()
                .setAlias(ImageInfo.BUILD_NAME)
                .setName(image)
                .setEntrypoint(Lists.newArrayList());

        RunnerJobInfo runnerJobInfo = createRunnerJob(repo, pipeline, jobRecord, pipeline.getResourceType(),
                pipeline.getClusterName(), ExecutorType.CONTAINER, buildImageInfo, envs, commonJob);

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
        log.info("cancel job:[{}] triggerUser:[{}]", jobRecord, triggerUser);
        sendCancelJobLog(jobRecord, triggerUser);
        schedulerClient.cancelRunnerJob(jobRecord.getTaskId());
    }

    @Override
    public void jobStartCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message) {
    }

    @Override
    public void jobSuccessCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data) {
        super.jobSuccessCallback(pipelineRecord, jobRecord, message, data);
    }

    @Override
    public void jobFailedCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data) {
        super.jobFailedCallback(pipelineRecord, jobRecord, message, data);
    }

    @Override
    public void jobAbortCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data) {
        super.jobAbortCallback(pipelineRecord, jobRecord, message, data);
    }

}