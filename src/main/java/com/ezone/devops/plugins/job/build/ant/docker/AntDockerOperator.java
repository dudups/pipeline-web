package com.ezone.devops.plugins.job.build.ant.docker;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.util.FreemarkerUtil;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.enums.ArtifactType;
import com.ezone.devops.plugins.job.AbstractPluginOperator;
import com.ezone.devops.plugins.job.PluginOperator;
import com.ezone.devops.plugins.job.build.ant.docker.bean.AntDockerConfigBean;
import com.ezone.devops.plugins.job.enums.RegistryType;
import com.ezone.devops.plugins.job.enums.VersionType;
import com.ezone.devops.plugins.service.ArtifactInfoService;
import com.ezone.devops.scheduler.bean.DockerCredential;
import com.ezone.devops.scheduler.bean.ImageInfo;
import com.ezone.devops.scheduler.client.response.RunnerJobInfo;
import com.ezone.devops.scheduler.enums.ExecutorType;
import com.ezone.devops.scheduler.job.AntDockerStep;
import com.ezone.devops.scheduler.job.CommonJob;
import com.ezone.devops.scheduler.job.DockerBuildStep;
import com.ezone.devops.scheduler.job.SpotBugScanStep;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AntDockerOperator extends AbstractPluginOperator implements PluginOperator {

    private static final String DEFAULT_TAG = "latest";
    @Autowired
    private AntDockerDataOperator antDockerDataOperator;
    @Autowired
    private ArtifactInfoService artifactInfoService;

    @Override
    public boolean execute(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        Long companyId = repo.getCompanyId();
        Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);

        AntDockerConfigBean antDockerConfigBean = antDockerDataOperator.getRealJob(jobRecord.getPluginId());

        List<DockerCredential> dockerCredentials = Lists.newArrayList();

        String registryUrl = null;
        if (antDockerConfigBean.getPushRegistryType() == RegistryType.EXTERNAL) {
            DockerCredential externalDockerCredential = getDockerCredential(companyId, jobRecord, antDockerConfigBean.getDockerRepo());
            dockerCredentials.add(externalDockerCredential);
            registryUrl = externalDockerCredential.getRegistryUrl();
        }

        String dockerTag = getDockerTag(envs, pipelineRecord, antDockerConfigBean);
        if (StringUtils.isBlank(dockerTag)) {
            dockerTag = DEFAULT_TAG;
        }

        DockerBuildStep dockerBuildStep = new DockerBuildStep();
        dockerBuildStep.setPushImage(true);
        dockerBuildStep.setCredentials(dockerCredentials);
        dockerBuildStep.setDockerRepo(antDockerConfigBean.getDockerRepo());
        dockerBuildStep.setDockerfile(antDockerConfigBean.getDockerfile());
        dockerBuildStep.setDockerContext(antDockerConfigBean.getDockerContext());
        dockerBuildStep.setRegistryType(antDockerConfigBean.getPushRegistryType());
        dockerBuildStep.setRegistryUrl(registryUrl);
        dockerBuildStep.setDockerImageName(antDockerConfigBean.getDockerImageName());
        dockerBuildStep.setPlatform(antDockerConfigBean.getPlatform());
        dockerBuildStep.setArch(antDockerConfigBean.getArch());
        dockerBuildStep.setDockerTag(dockerTag);

        SpotBugScanStep spotBugScanStep = new SpotBugScanStep();
        spotBugScanStep.setEnableScan(antDockerConfigBean.isEnableScan());
        if (antDockerConfigBean.isEnableScan()) {
            spotBugScanStep.setOutputPath(antDockerConfigBean.getOutputPath());
            spotBugScanStep.setScanLevel(antDockerConfigBean.getScanLevel());
            spotBugScanStep.setRulesetId(antDockerConfigBean.getRulesetId());
            spotBugScanStep.setFilterDirs(antDockerConfigBean.getFilterDirs());
            spotBugScanStep.setScanPkgNames(antDockerConfigBean.getScanPkgNames());
        }

        AntDockerStep antDockerStep = new AntDockerStep(dockerBuildStep, spotBugScanStep);

        CommonJob<AntDockerStep> commonJob = new CommonJob<AntDockerStep>()
                .setCloneMode(antDockerConfigBean.getCloneMode())
                .setJobType(jobRecord.getJobType())
                .setDockerCredentials(dockerCredentials)
                .setCommand(antDockerConfigBean.getCommand())
                .setSteps(antDockerStep);

        String image = getBuildImageUrl(antDockerConfigBean.getBuildImageId());
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

    private String getDockerTag(Map<String, String> envs, PipelineRecord pipelineRecord, AntDockerConfigBean antDockerConfigBean) {
        if (antDockerConfigBean.getDockerVersionType() == VersionType.CUSTOM_VERSION) {
            return FreemarkerUtil.parseExpression(antDockerConfigBean.getDockerTag(), envs);
        } else if (antDockerConfigBean.getDockerVersionType() == VersionType.RELEASE) {
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
        AntDockerConfigBean antDockerConfigBean = antDockerDataOperator.getRealJob(jobRecord.getPluginId());
        if (antDockerConfigBean.getPushRegistryType() != RegistryType.EXTERNAL) {
            Pipeline pipeline = pipelineService.getByIdIfPresent(pipelineRecord.getPipelineId());
            RepoVo repo = repoService.getByRepoKey(pipeline.getCompanyId(), jobRecord.getRepoKey());
            Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);
            String dockerTag = getDockerTag(envs, pipelineRecord, antDockerConfigBean);
            if (StringUtils.isBlank(dockerTag)) {
                dockerTag = DEFAULT_TAG;
            }
            artifactInfoService.saveArtifactInfo(pipelineRecord, ArtifactType.DOCKER, antDockerConfigBean.getDockerRepo(),
                    antDockerConfigBean.getDockerImageName(), dockerTag);
        }

        super.jobSuccessCallback(pipelineRecord, jobRecord, message, data);
    }

    @Override
    public void jobAbortCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data) {
        super.jobAbortCallback(pipelineRecord, jobRecord, message, data);
    }
}