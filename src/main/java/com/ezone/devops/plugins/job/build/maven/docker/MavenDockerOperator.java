package com.ezone.devops.plugins.job.build.maven.docker;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.util.FreemarkerUtil;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.enums.ArtifactType;
import com.ezone.devops.plugins.job.AbstractPluginOperator;
import com.ezone.devops.plugins.job.PluginOperator;
import com.ezone.devops.plugins.job.build.maven.docker.bean.MavenDockerConfigBean;
import com.ezone.devops.plugins.job.enums.RegistryType;
import com.ezone.devops.plugins.job.enums.VersionType;
import com.ezone.devops.plugins.service.ArtifactInfoService;
import com.ezone.devops.scheduler.bean.ContainerVolume;
import com.ezone.devops.scheduler.bean.DockerCredential;
import com.ezone.devops.scheduler.bean.ImageInfo;
import com.ezone.devops.scheduler.client.response.RunnerJobInfo;
import com.ezone.devops.scheduler.enums.ExecutorType;
import com.ezone.devops.scheduler.job.CommonJob;
import com.ezone.devops.scheduler.job.DockerBuildStep;
import com.ezone.devops.scheduler.job.MavenDockerStep;
import com.ezone.devops.scheduler.job.SpotBugScanStep;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class MavenDockerOperator extends AbstractPluginOperator implements PluginOperator {

    private static final String DEFAULT_TAG = "latest";
    @Autowired
    private MavenDockerDataOperator mavenDockerDataOperator;
    @Autowired
    private ArtifactInfoService artifactInfoService;

    @Override
    public boolean execute(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        Long companyId = repo.getCompanyId();
        Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);

        MavenDockerConfigBean mavenDockerConfigBean = mavenDockerDataOperator.getRealJob(jobRecord.getPluginId());

        List<DockerCredential> dockerCredentials = Lists.newArrayList();

        String registryUrl = null;
        if (mavenDockerConfigBean.getPushRegistryType() == RegistryType.EXTERNAL) {
            DockerCredential externalDockerCredential = getDockerCredential(companyId, jobRecord, mavenDockerConfigBean.getDockerRepo());
            dockerCredentials.add(externalDockerCredential);
            registryUrl = externalDockerCredential.getRegistryUrl();
        }

        String dockerTag = getDockerTag(envs, pipelineRecord, mavenDockerConfigBean);
        if (StringUtils.isBlank(dockerTag)) {
            dockerTag = DEFAULT_TAG;
        }

        DockerBuildStep dockerBuildStep = new DockerBuildStep()
                .setCredentials(dockerCredentials)
                .setPushImage(true)
                .setDockerRepo(mavenDockerConfigBean.getDockerRepo())
                .setDockerfile(mavenDockerConfigBean.getDockerfile())
                .setDockerContext(mavenDockerConfigBean.getDockerContext())
                .setRegistryType(mavenDockerConfigBean.getPushRegistryType())
                .setRegistryUrl(registryUrl)
                .setDockerImageName(mavenDockerConfigBean.getDockerImageName())
                .setPlatform(mavenDockerConfigBean.getPlatform())
                .setArch(mavenDockerConfigBean.getArch())
                .setDockerTag(dockerTag);

        SpotBugScanStep spotBugScanStep = new SpotBugScanStep();
        spotBugScanStep.setEnableScan(mavenDockerConfigBean.isEnableScan());
        if (mavenDockerConfigBean.isEnableScan()) {
            spotBugScanStep.setOutputPath(mavenDockerConfigBean.getOutputPath());
            spotBugScanStep.setScanLevel(mavenDockerConfigBean.getScanLevel());
            spotBugScanStep.setRulesetId(mavenDockerConfigBean.getRulesetId());
            spotBugScanStep.setFilterDirs(mavenDockerConfigBean.getFilterDirs());
            spotBugScanStep.setScanPkgNames(mavenDockerConfigBean.getScanPkgNames());
        }

        MavenDockerStep mavenDockerStep = new MavenDockerStep();
        mavenDockerStep.setDockerBuildStep(dockerBuildStep);
        mavenDockerStep.setSpotBugScanStep(spotBugScanStep);

        Set<ContainerVolume> tempDirs = Sets.newHashSet();
        if (mavenDockerConfigBean.isAutoGenerateConfig()) {
            mavenDockerStep.setAutoGenerateConfig(mavenDockerConfigBean.isAutoGenerateConfig());
            mavenDockerStep.setPublicRepoNames(mavenDockerConfigBean.getPublicRepoNames());
            mavenDockerStep.setPrivateRepoNames(mavenDockerConfigBean.getPrivateRepoNames());
            mavenDockerStep.setUserHomePath(mavenDockerConfigBean.getUserHomePath());

            String destination;
            if (StringUtils.endsWith(mavenDockerConfigBean.getUserHomePath(), "/")) {
                destination = mavenDockerConfigBean.getUserHomePath() + ".m2";
            } else {
                destination = mavenDockerConfigBean.getUserHomePath() + "/.m2";
            }

            ContainerVolume containerVolume = new ContainerVolume();
            containerVolume.setName(MAVEN_VOLUME_NAME);
            containerVolume.setSource(destination);
            containerVolume.setDestination(destination);
            tempDirs.add(containerVolume);
        }

        CommonJob<MavenDockerStep> commonJob = new CommonJob<MavenDockerStep>()
                .setCloneMode(mavenDockerConfigBean.getCloneMode())
                .setJobType(jobRecord.getJobType())
                .setDockerCredentials(dockerCredentials)
                .setTempDirs(tempDirs)
                .setCommand(mavenDockerConfigBean.getCommand())
                .setSteps(mavenDockerStep);

        String image = getBuildImageUrl(mavenDockerConfigBean.getBuildImageId());
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

    private String getDockerTag(Map<String, String> envs, PipelineRecord pipelineRecord, MavenDockerConfigBean mavenDockerConfigBean) {
        if (mavenDockerConfigBean.getDockerVersionType() == VersionType.CUSTOM_VERSION) {
            return FreemarkerUtil.parseExpression(mavenDockerConfigBean.getDockerTag(), envs);
        } else if (mavenDockerConfigBean.getDockerVersionType() == VersionType.RELEASE) {
            return pipelineRecord.getReleaseVersion();
        } else {
            return pipelineRecord.getSnapshotVersion();
        }
    }

    @Override
    public void cancel(PipelineRecord pipelineRecord, JobRecord jobRecord, String triggerUser) {
        sendCancelJobLog(jobRecord, triggerUser);
        schedulerClient.cancelRunnerJob(jobRecord.getTaskId());
    }

    @Override
    public void jobStartCallback(PipelineRecord pipelineRecord, JobRecord jobBuild, String message) {
    }

    @Override
    public void jobSuccessCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data) {
        MavenDockerConfigBean mavenDockerConfigBean = mavenDockerDataOperator.getRealJob(jobRecord.getPluginId());
        if (mavenDockerConfigBean.getPushRegistryType() != RegistryType.EXTERNAL) {
            Pipeline pipeline = pipelineService.getByIdIfPresent(pipelineRecord.getPipelineId());
            RepoVo repo = repoService.getByRepoKey(pipeline.getCompanyId(), jobRecord.getRepoKey());
            Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);
            String dockerTag = getDockerTag(envs, pipelineRecord, mavenDockerConfigBean);
            if (StringUtils.isBlank(dockerTag)) {
                dockerTag = DEFAULT_TAG;
            }
            artifactInfoService.saveArtifactInfo(pipelineRecord, ArtifactType.DOCKER, mavenDockerConfigBean.getDockerRepo(),
                    mavenDockerConfigBean.getDockerImageName(), dockerTag);
        }

        super.jobSuccessCallback(pipelineRecord, jobRecord, message, data);
    }

    @Override
    public void jobAbortCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data) {
        super.jobAbortCallback(pipelineRecord, jobRecord, message, data);
    }
}