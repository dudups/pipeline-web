package com.ezone.devops.plugins.job.build.dotnet.file;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.util.FreemarkerUtil;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.enums.ArtifactType;
import com.ezone.devops.plugins.job.AbstractPluginOperator;
import com.ezone.devops.plugins.job.PluginOperator;
import com.ezone.devops.plugins.job.build.dotnet.file.bean.Dotnet;
import com.ezone.devops.plugins.job.enums.VersionType;
import com.ezone.devops.plugins.service.ArtifactInfoService;
import com.ezone.devops.scheduler.bean.ContainerVolume;
import com.ezone.devops.scheduler.bean.ImageInfo;
import com.ezone.devops.scheduler.client.response.RunnerJobInfo;
import com.ezone.devops.scheduler.enums.ExecutorType;
import com.ezone.devops.scheduler.job.CommonJob;
import com.ezone.devops.scheduler.job.DotnetStep;
import com.ezone.devops.scheduler.job.FileUploadStep;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class DotnetOperator extends AbstractPluginOperator implements PluginOperator {

    @Autowired
    private DotnetDataOperator dotnetDataOperator;
    @Autowired
    private ArtifactInfoService artifactInfoService;

    @Override
    public boolean execute(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);

        Dotnet dotnet = dotnetDataOperator.getRealJob(jobRecord.getPluginId());
        String image = getBuildImageUrl(dotnet.getBuildImageId());

        FileUploadStep fileUploadStep = new FileUploadStep();
        fileUploadStep.setUploadArtifact(dotnet.isUploadArtifact());
        if (dotnet.isUploadArtifact()) {
            String version = pipelineRecord.getSnapshotVersion();
            if (dotnet.getVersionType() == VersionType.CUSTOM_VERSION) {
                version = FreemarkerUtil.parseExpression(dotnet.getCustomVersion(), envs);
                if (StringUtils.isBlank(version)) {
                    super.noticeJobFailed(pipelineRecord, jobRecord, "解析version出错");
                    return false;
                }
            } else if (dotnet.getVersionType() == VersionType.RELEASE) {
                version = pipelineRecord.getReleaseVersion();
                if (StringUtils.isBlank(version)) {
                    super.noticeJobFailed(pipelineRecord, jobRecord, "流水线尚未发版，无法使用发布版本");
                    return false;
                }
            }


            fileUploadStep.setPkgRepo(dotnet.getPkgRepo());
            fileUploadStep.setPkgName(dotnet.getPkgName());
            fileUploadStep.setArtifactPath(dotnet.getArtifactPath());
            fileUploadStep.setArtifactVersion(version);
        }

        DotnetStep dotnetStep = new DotnetStep();
        dotnetStep.setFileUploadStep(fileUploadStep);

        Set<ContainerVolume> tempDirs = Sets.newHashSet();
        if (dotnet.isAutoGenerateConfig()) {
            dotnetStep.setAutoGenerateConfig(dotnet.isAutoGenerateConfig());
            dotnetStep.setPublicRepoNames(dotnet.getPublicRepoNames());
            dotnetStep.setPrivateRepoNames(dotnet.getPrivateRepoNames());
            dotnetStep.setUserHomePath(dotnet.getUserHomePath());

            String destination;
            if (StringUtils.equals(dotnet.getUserHomePath(), "/")) {
                destination = dotnet.getUserHomePath() + ".nuget/NuGet";
            } else {
                destination = dotnet.getUserHomePath() + "/.nuget/NuGet";
            }

            ContainerVolume containerVolume = new ContainerVolume();
            containerVolume.setName(NUGET_VOLUME_NAME);
            containerVolume.setSource(destination);
            containerVolume.setDestination(destination);
            tempDirs.add(containerVolume);
        }

        CommonJob<DotnetStep> commonJob = new CommonJob<DotnetStep>()
                .setCloneMode(dotnet.getCloneMode())
                .setJobType(jobRecord.getJobType())
                .setCommand(dotnet.getCommand())
                .setTempDirs(tempDirs)
                .setSteps(dotnetStep);

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
        Dotnet dotnet = dotnetDataOperator.getRealJob(jobRecord.getPluginId());
        if (dotnet.isUploadArtifact()) {
            String version = pipelineRecord.getSnapshotVersion();
            if (dotnet.getVersionType() == VersionType.CUSTOM_VERSION) {
                RepoVo repo = repoService.getByRepoKey(pipelineRecord.getCompanyId(), pipelineRecord.getRepoKey());
                Pipeline pipeline = pipelineService.getByIdIfPresent(pipelineRecord.getPipelineId());
                Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);
                version = FreemarkerUtil.parseExpression(dotnet.getCustomVersion(), envs);
            } else if (dotnet.getVersionType() == VersionType.RELEASE) {
                version = pipelineRecord.getReleaseVersion();
            }
            artifactInfoService.saveArtifactInfo(pipelineRecord, ArtifactType.RAW, dotnet.getPkgRepo(), dotnet.getPkgName(), version);
        }
        super.jobSuccessCallback(pipelineRecord, jobRecord, message, data);
    }

    @Override
    public void jobAbortCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data) {
        super.jobAbortCallback(pipelineRecord, jobRecord, message, data);
    }
}