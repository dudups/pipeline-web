package com.ezone.devops.plugins.job.build.maven.file;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.util.FreemarkerUtil;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.enums.ArtifactType;
import com.ezone.devops.plugins.job.AbstractPluginOperator;
import com.ezone.devops.plugins.job.PluginOperator;
import com.ezone.devops.plugins.job.build.maven.file.bean.MavenConfigBean;
import com.ezone.devops.plugins.job.enums.VersionType;
import com.ezone.devops.plugins.service.ArtifactInfoService;
import com.ezone.devops.scheduler.bean.ContainerVolume;
import com.ezone.devops.scheduler.bean.ImageInfo;
import com.ezone.devops.scheduler.client.response.RunnerJobInfo;
import com.ezone.devops.scheduler.enums.ExecutorType;
import com.ezone.devops.scheduler.job.CommonJob;
import com.ezone.devops.scheduler.job.FileUploadStep;
import com.ezone.devops.scheduler.job.MavenStep;
import com.ezone.devops.scheduler.job.SpotBugScanStep;
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
public class MavenOperator extends AbstractPluginOperator implements PluginOperator {

    @Autowired
    private MavenDataOperator mavenDataOperator;
    @Autowired
    private ArtifactInfoService artifactInfoService;

    @Override
    public boolean execute(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);

        MavenConfigBean mavenConfigBean = mavenDataOperator.getRealJob(jobRecord.getPluginId());
        String image = getBuildImageUrl(mavenConfigBean.getBuildImageId());

        FileUploadStep fileUploadStep = new FileUploadStep();
        fileUploadStep.setUploadArtifact(mavenConfigBean.isUploadArtifact());
        if (mavenConfigBean.isUploadArtifact()) {
            String version = pipelineRecord.getSnapshotVersion();
            if (mavenConfigBean.getVersionType() == VersionType.CUSTOM_VERSION) {
                version = FreemarkerUtil.parseExpression(mavenConfigBean.getCustomVersion(), envs);
                if (StringUtils.isBlank(version)) {
                    super.noticeJobFailed(pipelineRecord, jobRecord, "解析version出错");
                    return false;
                }
            } else if (mavenConfigBean.getVersionType() == VersionType.RELEASE) {
                version = pipelineRecord.getReleaseVersion();
                if (StringUtils.isBlank(version)) {
                    super.noticeJobFailed(pipelineRecord, jobRecord, "流水线尚未发版，无法使用发布版本");
                    return false;
                }
            }

            fileUploadStep.setPkgRepo(mavenConfigBean.getPkgRepo());
            fileUploadStep.setPkgName(mavenConfigBean.getPkgName());
            fileUploadStep.setArtifactPath(mavenConfigBean.getArtifactPath());
            fileUploadStep.setArtifactVersion(version);
        }

        SpotBugScanStep spotBugScanStep = new SpotBugScanStep();
        spotBugScanStep.setEnableScan(mavenConfigBean.isEnableScan());
        if (mavenConfigBean.isEnableScan()) {
            spotBugScanStep.setOutputPath(mavenConfigBean.getOutputPath());
            spotBugScanStep.setScanLevel(mavenConfigBean.getScanLevel());
            spotBugScanStep.setRulesetId(mavenConfigBean.getRulesetId());
            spotBugScanStep.setFilterDirs(mavenConfigBean.getFilterDirs());
            spotBugScanStep.setScanPkgNames(mavenConfigBean.getScanPkgNames());
        }

        MavenStep mavenStep = new MavenStep();
        mavenStep.setFileUploadStep(fileUploadStep);
        mavenStep.setSpotBugScanStep(spotBugScanStep);

        Set<ContainerVolume> tempDirs = Sets.newHashSet();
        if (mavenConfigBean.isAutoGenerateConfig()) {
            mavenStep.setAutoGenerateConfig(mavenConfigBean.isAutoGenerateConfig());
            mavenStep.setPublicRepoNames(mavenConfigBean.getPublicRepoNames());
            mavenStep.setPrivateRepoNames(mavenConfigBean.getPrivateRepoNames());
            mavenStep.setUserHomePath(mavenConfigBean.getUserHomePath());

            String destination;
            if (StringUtils.endsWith(mavenConfigBean.getUserHomePath(), "/")) {
                destination = mavenConfigBean.getUserHomePath() + ".m2";
            } else {
                destination = mavenConfigBean.getUserHomePath() + "/.m2";
            }

            ContainerVolume containerVolume = new ContainerVolume();
            containerVolume.setName(MAVEN_VOLUME_NAME);
            containerVolume.setSource(destination);
            containerVolume.setDestination(destination);
            tempDirs.add(containerVolume);
        }

        CommonJob<MavenStep> commonJob = new CommonJob<MavenStep>()
                .setCloneMode(mavenConfigBean.getCloneMode())
                .setJobType(jobRecord.getJobType())
                .setCommand(mavenConfigBean.getCommand())
                .setTempDirs(tempDirs)
                .setSteps(mavenStep);

        ImageInfo buildImageInfo = new ImageInfo()
                .setAlias(ImageInfo.BUILD_NAME)
                .setName(image)
                .setEntrypoint(Lists.newArrayList());

        RunnerJobInfo runnerJobInfo = createRunnerJob(repo, pipeline, jobRecord,
                pipeline.getResourceType(), pipeline.getClusterName(), ExecutorType.CONTAINER,
                buildImageInfo, envs, commonJob);

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
        MavenConfigBean mavenConfigBean = mavenDataOperator.getRealJob(jobRecord.getPluginId());
        if (mavenConfigBean.isUploadArtifact()) {
            String version = pipelineRecord.getSnapshotVersion();
            if (mavenConfigBean.getVersionType() == VersionType.CUSTOM_VERSION) {
                RepoVo repo = repoService.getByRepoKey(pipelineRecord.getCompanyId(), pipelineRecord.getRepoKey());
                Pipeline pipeline = pipelineService.getByIdIfPresent(pipelineRecord.getPipelineId());
                Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);
                version = FreemarkerUtil.parseExpression(mavenConfigBean.getCustomVersion(), envs);
            } else if (mavenConfigBean.getVersionType() == VersionType.RELEASE) {
                version = pipelineRecord.getReleaseVersion();
            }
            artifactInfoService.saveArtifactInfo(pipelineRecord, ArtifactType.RAW, mavenConfigBean.getPkgRepo(), mavenConfigBean.getPkgName(), version);
        }
        super.jobSuccessCallback(pipelineRecord, jobRecord, message, data);
    }

    @Override
    public void jobAbortCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data) {
        super.jobAbortCallback(pipelineRecord, jobRecord, message, data);
    }
}