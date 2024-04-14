package com.ezone.devops.plugins.job.build.python.docker;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.util.FreemarkerUtil;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.enums.ArtifactType;
import com.ezone.devops.plugins.job.AbstractPluginOperator;
import com.ezone.devops.plugins.job.PluginOperator;
import com.ezone.devops.plugins.job.build.python.docker.bean.PythonDocker;
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
import com.ezone.devops.scheduler.job.PythonDockerStep;
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
public class PythonDockerOperator extends AbstractPluginOperator implements PluginOperator {

    private static final String DEFAULT_TAG = "latest";
    @Autowired
    private PythonDockerDataOperator pythonDockerDataOperator;
    @Autowired
    private ArtifactInfoService artifactInfoService;

    @Override
    public boolean execute(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        Long companyId = repo.getCompanyId();
        Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);

        PythonDocker pythonDocker = pythonDockerDataOperator.getRealJob(jobRecord.getPluginId());

        List<DockerCredential> dockerCredentials = Lists.newArrayList();

        String registryUrl = null;
        if (pythonDocker.getPushRegistryType() == RegistryType.EXTERNAL) {
            DockerCredential externalDockerCredential = getDockerCredential(companyId, jobRecord, pythonDocker.getDockerRepo());
            dockerCredentials.add(externalDockerCredential);
            registryUrl = externalDockerCredential.getRegistryUrl();
        }

        String dockerTag = getDockerTag(envs, pipelineRecord, pythonDocker);
        if (StringUtils.isBlank(dockerTag)) {
            dockerTag = DEFAULT_TAG;
        }

        DockerBuildStep dockerBuildStep = new DockerBuildStep()
                .setCredentials(dockerCredentials)
                .setPushImage(true)
                .setDockerRepo(pythonDocker.getDockerRepo())
                .setDockerfile(pythonDocker.getDockerfile())
                .setDockerContext(pythonDocker.getDockerContext())
                .setRegistryType(pythonDocker.getPushRegistryType())
                .setRegistryUrl(registryUrl)
                .setDockerImageName(pythonDocker.getDockerImageName())
                .setPlatform(pythonDocker.getPlatform())
                .setArch(pythonDocker.getArch())
                .setDockerTag(dockerTag);
        PythonDockerStep pythonDockerStep = new PythonDockerStep();
        pythonDockerStep.setDockerBuildStep(dockerBuildStep);

        Set<ContainerVolume> tempDirs = Sets.newHashSet();
        if (pythonDocker.isAutoGenerateConfig()) {
            pythonDockerStep.setAutoGenerateConfig(pythonDocker.isAutoGenerateConfig());
            pythonDockerStep.setPublicRepoNames(pythonDocker.getPublicRepoNames());
            pythonDockerStep.setPrivateRepoNames(pythonDocker.getPrivateRepoNames());
            pythonDockerStep.setUserHomePath(pythonDocker.getUserHomePath());

            String pypiConfigDestination;
            if (StringUtils.equals(pythonDocker.getUserHomePath(), "/")) {
                pypiConfigDestination = pythonDocker.getUserHomePath() + ".pip";
            } else {
                pypiConfigDestination = pythonDocker.getUserHomePath() + "/.pip";
            }
            ContainerVolume pypiConfigContainerVolume = new ContainerVolume();
            pypiConfigContainerVolume.setName(PYPI_VOLUME_NAME);
            pypiConfigContainerVolume.setSource(pypiConfigDestination);
            pypiConfigContainerVolume.setDestination(pypiConfigDestination);
            tempDirs.add(pypiConfigContainerVolume);

            String pypircConfigDestination = pythonDocker.getUserHomePath();
            ContainerVolume pypircConfigContainerVolume = new ContainerVolume();
            pypircConfigContainerVolume.setName(PYPIRC_VOLUME_NAME);
            pypircConfigContainerVolume.setSource(pypircConfigDestination);
            pypircConfigContainerVolume.setDestination(pypircConfigDestination);
            tempDirs.add(pypircConfigContainerVolume);
        }

        CommonJob<PythonDockerStep> commonJob = new CommonJob<PythonDockerStep>()
                .setCloneMode(pythonDocker.getCloneMode())
                .setJobType(jobRecord.getJobType())
                .setDockerCredentials(dockerCredentials)
                .setCommand(pythonDocker.getCommand())
                .setTempDirs(tempDirs)
                .setSteps(pythonDockerStep);

        String image = getBuildImageUrl(pythonDocker.getBuildImageId());
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

    private String getDockerTag(Map<String, String> envs, PipelineRecord pipelineRecord, PythonDocker pythonDocker) {
        if (pythonDocker.getDockerVersionType() == VersionType.CUSTOM_VERSION) {
            return FreemarkerUtil.parseExpression(pythonDocker.getDockerTag(), envs);
        } else if (pythonDocker.getDockerVersionType() == VersionType.RELEASE) {
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
        PythonDocker pythonDocker = pythonDockerDataOperator.getRealJob(jobRecord.getPluginId());
        if (pythonDocker.getPushRegistryType() != RegistryType.EXTERNAL) {
            Pipeline pipeline = pipelineService.getByIdIfPresent(pipelineRecord.getPipelineId());
            RepoVo repo = repoService.getByRepoKey(pipeline.getCompanyId(), jobRecord.getRepoKey());
            Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);
            String dockerTag = getDockerTag(envs, pipelineRecord, pythonDocker);
            if (StringUtils.isBlank(dockerTag)) {
                dockerTag = DEFAULT_TAG;
            }
            artifactInfoService.saveArtifactInfo(pipelineRecord, ArtifactType.DOCKER, pythonDocker.getDockerRepo(),
                    pythonDocker.getDockerImageName(), dockerTag);
        }

        super.jobSuccessCallback(pipelineRecord, jobRecord, message, data);
    }

    @Override
    public void jobAbortCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data) {
        super.jobAbortCallback(pipelineRecord, jobRecord, message, data);
    }
}