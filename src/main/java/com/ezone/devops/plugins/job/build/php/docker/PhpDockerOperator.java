package com.ezone.devops.plugins.job.build.php.docker;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.util.FreemarkerUtil;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.enums.ArtifactType;
import com.ezone.devops.plugins.job.AbstractPluginOperator;
import com.ezone.devops.plugins.job.PluginOperator;
import com.ezone.devops.plugins.job.build.php.docker.bean.PhpDocker;
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
import com.ezone.devops.scheduler.job.PhpDockerStep;
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
public class PhpDockerOperator extends AbstractPluginOperator implements PluginOperator {

    private static final String DEFAULT_TAG = "latest";
    @Autowired
    private PhpDockerDataOperator phpDockerDataOperator;
    @Autowired
    private ArtifactInfoService artifactInfoService;

    @Override
    public boolean execute(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        Long companyId = repo.getCompanyId();
        Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);

        PhpDocker phpDocker = phpDockerDataOperator.getRealJob(jobRecord.getPluginId());

        List<DockerCredential> dockerCredentials = Lists.newArrayList();

        String registryUrl = null;
        if (phpDocker.getPushRegistryType() == RegistryType.EXTERNAL) {
            DockerCredential externalDockerCredential = getDockerCredential(companyId, jobRecord, phpDocker.getDockerRepo());
            dockerCredentials.add(externalDockerCredential);
            registryUrl = externalDockerCredential.getRegistryUrl();
        }

        String dockerTag = getDockerTag(envs, pipelineRecord, phpDocker);
        if (StringUtils.isBlank(dockerTag)) {
            dockerTag = DEFAULT_TAG;
        }

        DockerBuildStep dockerBuildStep = new DockerBuildStep()
                .setCredentials(dockerCredentials)
                .setPushImage(true)
                .setDockerRepo(phpDocker.getDockerRepo())
                .setDockerfile(phpDocker.getDockerfile())
                .setDockerContext(phpDocker.getDockerContext())
                .setRegistryType(phpDocker.getPushRegistryType())
                .setRegistryUrl(registryUrl)
                .setDockerImageName(phpDocker.getDockerImageName())
                .setPlatform(phpDocker.getPlatform())
                .setArch(phpDocker.getArch())
                .setDockerTag(dockerTag);

        PhpDockerStep phpDockerStep = new PhpDockerStep();
        phpDockerStep.setDockerBuildStep(dockerBuildStep);

        Set<ContainerVolume> tempDirs = Sets.newHashSet();
        if (phpDocker.isAutoGenerateConfig()) {
            phpDockerStep.setAutoGenerateConfig(phpDocker.isAutoGenerateConfig());
            phpDockerStep.setPublicRepoNames(phpDocker.getPublicRepoNames());
            phpDockerStep.setPrivateRepoNames(phpDocker.getPrivateRepoNames());
            phpDockerStep.setUserHomePath(phpDocker.getUserHomePath());

            String destination;
            if (StringUtils.endsWith(phpDocker.getUserHomePath(), "/")) {
                destination = phpDocker.getUserHomePath() + ".composer";
            } else {
                destination = phpDocker.getUserHomePath() + "/.composer";
            }

            ContainerVolume containerVolume = new ContainerVolume();
            containerVolume.setName(COMPOSER_VOLUME_NAME);
            containerVolume.setSource(destination);
            containerVolume.setDestination(destination);
            tempDirs.add(containerVolume);
        }

        CommonJob<PhpDockerStep> commonJob = new CommonJob<PhpDockerStep>()
                .setCloneMode(phpDocker.getCloneMode())
                .setJobType(jobRecord.getJobType())
                .setDockerCredentials(dockerCredentials)
                .setCommand(phpDocker.getCommand())
                .setTempDirs(tempDirs)
                .setSteps(phpDockerStep);

        String image = getBuildImageUrl(phpDocker.getBuildImageId());
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

    private String getDockerTag(Map<String, String> envs, PipelineRecord pipelineRecord, PhpDocker phpDocker) {
        if (phpDocker.getDockerVersionType() == VersionType.CUSTOM_VERSION) {
            return FreemarkerUtil.parseExpression(phpDocker.getDockerTag(), envs);
        } else if (phpDocker.getDockerVersionType() == VersionType.RELEASE) {
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
        PhpDocker phpDocker = phpDockerDataOperator.getRealJob(jobRecord.getPluginId());
        if (phpDocker.getPushRegistryType() != RegistryType.EXTERNAL) {
            Pipeline pipeline = pipelineService.getByIdIfPresent(pipelineRecord.getPipelineId());
            RepoVo repo = repoService.getByRepoKey(pipeline.getCompanyId(), jobRecord.getRepoKey());
            Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);
            String dockerTag = getDockerTag(envs, pipelineRecord, phpDocker);
            if (StringUtils.isBlank(dockerTag)) {
                dockerTag = DEFAULT_TAG;
            }
            artifactInfoService.saveArtifactInfo(pipelineRecord, ArtifactType.DOCKER, phpDocker.getDockerRepo(),
                    phpDocker.getDockerImageName(), dockerTag);
        }

        super.jobSuccessCallback(pipelineRecord, jobRecord, message, data);
    }

    @Override
    public void jobAbortCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data) {
        super.jobAbortCallback(pipelineRecord, jobRecord, message, data);
    }
}