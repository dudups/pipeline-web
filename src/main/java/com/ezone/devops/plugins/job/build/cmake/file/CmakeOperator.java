package com.ezone.devops.plugins.job.build.cmake.file;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.util.FreemarkerUtil;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.enums.ArtifactType;
import com.ezone.devops.plugins.job.AbstractPluginOperator;
import com.ezone.devops.plugins.job.PluginOperator;
import com.ezone.devops.plugins.job.build.cmake.file.bean.Cmake;
import com.ezone.devops.plugins.job.enums.VersionType;
import com.ezone.devops.plugins.service.ArtifactInfoService;
import com.ezone.devops.scheduler.bean.ImageInfo;
import com.ezone.devops.scheduler.client.response.RunnerJobInfo;
import com.ezone.devops.scheduler.enums.ExecutorType;
import com.ezone.devops.scheduler.job.CmakeStep;
import com.ezone.devops.scheduler.job.CommonJob;
import com.ezone.devops.scheduler.job.FileUploadStep;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class CmakeOperator extends AbstractPluginOperator implements PluginOperator {

    @Autowired
    private CmakeDataOperator cmakeDataOperator;
    @Autowired
    private ArtifactInfoService artifactInfoService;

    @Override
    public boolean execute(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);

        Cmake cmake = cmakeDataOperator.getRealJob(jobRecord.getPluginId());
        String image = getBuildImageUrl(cmake.getBuildImageId());

        FileUploadStep fileUploadStep = new FileUploadStep();
        fileUploadStep.setUploadArtifact(cmake.isUploadArtifact());
        if (cmake.isUploadArtifact()) {
            String version = pipelineRecord.getSnapshotVersion();
            if (cmake.getVersionType() == VersionType.CUSTOM_VERSION) {
                version = FreemarkerUtil.parseExpression(cmake.getCustomVersion(), envs);
                if (StringUtils.isBlank(version)) {
                    super.noticeJobFailed(pipelineRecord, jobRecord, "解析version出错");
                    return false;
                }
            } else if (cmake.getVersionType() == VersionType.RELEASE) {
                version = pipelineRecord.getReleaseVersion();
                if (StringUtils.isBlank(version)) {
                    super.noticeJobFailed(pipelineRecord, jobRecord, "流水线尚未发版，无法使用发布版本");
                    return false;
                }
            }

            fileUploadStep.setPkgRepo(cmake.getPkgRepo());
            fileUploadStep.setPkgName(cmake.getPkgName());
            fileUploadStep.setArtifactPath(cmake.getArtifactPath());
            fileUploadStep.setArtifactVersion(version);
        }

        CmakeStep cmakeStep = new CmakeStep(fileUploadStep);

        CommonJob<CmakeStep> commonJob = new CommonJob<CmakeStep>().setCloneMode(cmake.getCloneMode()).setJobType(jobRecord.getJobType()).setCommand(cmake.getCommand()).setSteps(cmakeStep);

        ImageInfo buildImageInfo = new ImageInfo().setAlias(ImageInfo.BUILD_NAME).setName(image).setEntrypoint(Lists.newArrayList());

        RunnerJobInfo runnerJobInfo = createRunnerJob(repo, pipeline, jobRecord, pipeline.getResourceType(), pipeline.getClusterName(), ExecutorType.CONTAINER, buildImageInfo, envs, commonJob);

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
        Cmake cmake = cmakeDataOperator.getRealJob(jobRecord.getPluginId());
        if (cmake.isUploadArtifact()) {
            String version = pipelineRecord.getSnapshotVersion();
            if (cmake.getVersionType() == VersionType.CUSTOM_VERSION) {
                RepoVo repo = repoService.getByRepoKey(pipelineRecord.getCompanyId(), pipelineRecord.getRepoKey());
                Pipeline pipeline = pipelineService.getByIdIfPresent(pipelineRecord.getPipelineId());
                Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);
                version = FreemarkerUtil.parseExpression(cmake.getCustomVersion(), envs);
            } else if (cmake.getVersionType() == VersionType.RELEASE) {
                version = pipelineRecord.getReleaseVersion();
            }
            artifactInfoService.saveArtifactInfo(pipelineRecord, ArtifactType.RAW, cmake.getPkgRepo(), cmake.getPkgName(), version);
        }
        super.jobSuccessCallback(pipelineRecord, jobRecord, message, data);
    }

    @Override
    public void jobAbortCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data) {
        super.jobAbortCallback(pipelineRecord, jobRecord, message, data);
    }
}