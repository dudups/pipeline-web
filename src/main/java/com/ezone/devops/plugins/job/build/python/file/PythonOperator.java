package com.ezone.devops.plugins.job.build.python.file;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.util.FreemarkerUtil;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.enums.ArtifactType;
import com.ezone.devops.plugins.job.AbstractPluginOperator;
import com.ezone.devops.plugins.job.PluginOperator;
import com.ezone.devops.plugins.job.build.python.file.bean.Python;
import com.ezone.devops.plugins.job.enums.VersionType;
import com.ezone.devops.plugins.service.ArtifactInfoService;
import com.ezone.devops.scheduler.bean.ContainerVolume;
import com.ezone.devops.scheduler.bean.ImageInfo;
import com.ezone.devops.scheduler.client.response.RunnerJobInfo;
import com.ezone.devops.scheduler.enums.ExecutorType;
import com.ezone.devops.scheduler.job.CommonJob;
import com.ezone.devops.scheduler.job.FileUploadStep;
import com.ezone.devops.scheduler.job.PythonStep;
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
public class PythonOperator extends AbstractPluginOperator implements PluginOperator {

    @Autowired
    private PythonDataOperator pythonDataOperator;
    @Autowired
    private ArtifactInfoService artifactInfoService;

    @Override
    public boolean execute(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);

        Python python = pythonDataOperator.getRealJob(jobRecord.getPluginId());
        String image = getBuildImageUrl(python.getBuildImageId());

        FileUploadStep fileUploadStep = new FileUploadStep();
        fileUploadStep.setUploadArtifact(python.isUploadArtifact());
        if (python.isUploadArtifact()) {
            String version = pipelineRecord.getSnapshotVersion();
            if (python.getVersionType() == VersionType.CUSTOM_VERSION) {
                version = FreemarkerUtil.parseExpression(python.getCustomVersion(), envs);
                if (StringUtils.isBlank(version)) {
                    super.noticeJobFailed(pipelineRecord, jobRecord, "解析version出错");
                    return false;
                }
            } else if (python.getVersionType() == VersionType.RELEASE) {
                version = pipelineRecord.getReleaseVersion();
                if (StringUtils.isBlank(version)) {
                    super.noticeJobFailed(pipelineRecord, jobRecord, "流水线尚未发版，无法使用发布版本");
                    return false;
                }
            }

            fileUploadStep.setPkgRepo(python.getPkgRepo());
            fileUploadStep.setPkgName(python.getPkgName());
            fileUploadStep.setArtifactPath(python.getArtifactPath());
            fileUploadStep.setArtifactVersion(version);
        }
        PythonStep pythonStep = new PythonStep();
        pythonStep.setFileUploadStep(fileUploadStep);

        Set<ContainerVolume> tempDirs = Sets.newHashSet();
        if (python.isAutoGenerateConfig()) {
            pythonStep.setAutoGenerateConfig(python.isAutoGenerateConfig());
            pythonStep.setPublicRepoNames(python.getPublicRepoNames());
            pythonStep.setPrivateRepoNames(python.getPrivateRepoNames());
            pythonStep.setUserHomePath(python.getUserHomePath());

            String pypiConfigDestination;
            if (StringUtils.equals(python.getUserHomePath(), "/")) {
                pypiConfigDestination = python.getUserHomePath() + ".pip";
            } else {
                pypiConfigDestination = python.getUserHomePath() + "/.pip";
            }
            ContainerVolume pypiConfigContainerVolume = new ContainerVolume();
            pypiConfigContainerVolume.setName(PYPI_VOLUME_NAME);
            pypiConfigContainerVolume.setSource(pypiConfigDestination);
            pypiConfigContainerVolume.setDestination(pypiConfigDestination);
            tempDirs.add(pypiConfigContainerVolume);

            String pypircConfigDestination = python.getUserHomePath();
            ContainerVolume pypircConfigContainerVolume = new ContainerVolume();
            pypircConfigContainerVolume.setName(PYPIRC_VOLUME_NAME);
            pypircConfigContainerVolume.setSource(pypircConfigDestination);
            pypircConfigContainerVolume.setDestination(pypircConfigDestination);
            tempDirs.add(pypircConfigContainerVolume);
        }

        CommonJob<PythonStep> commonJob = new CommonJob<PythonStep>()
                .setCloneMode(python.getCloneMode())
                .setJobType(jobRecord.getJobType())
                .setCommand(python.getCommand())
                .setTempDirs(tempDirs)
                .setSteps(pythonStep);

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
        Python python = pythonDataOperator.getRealJob(jobRecord.getPluginId());
        if (python.isUploadArtifact()) {
            String version = pipelineRecord.getSnapshotVersion();
            if (python.getVersionType() == VersionType.CUSTOM_VERSION) {
                RepoVo repo = repoService.getByRepoKey(pipelineRecord.getCompanyId(), pipelineRecord.getRepoKey());
                Pipeline pipeline = pipelineService.getByIdIfPresent(pipelineRecord.getPipelineId());
                Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);
                version = FreemarkerUtil.parseExpression(python.getCustomVersion(), envs);
            } else if (python.getVersionType() == VersionType.RELEASE) {
                version = pipelineRecord.getReleaseVersion();
            }

            artifactInfoService.saveArtifactInfo(pipelineRecord, ArtifactType.RAW, python.getPkgRepo(), python.getPkgName(), version);
        }
        super.jobSuccessCallback(pipelineRecord, jobRecord, message, data);
    }

    @Override
    public void jobAbortCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data) {
        super.jobAbortCallback(pipelineRecord, jobRecord, message, data);
    }
}