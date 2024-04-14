package com.ezone.devops.plugins.job.other.docker;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.clients.response.DockerImageInfo;
import com.ezone.devops.pipeline.clients.response.DockerImageInfoResponse;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.util.FreemarkerUtil;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.enums.ArtifactType;
import com.ezone.devops.plugins.job.AbstractPluginOperator;
import com.ezone.devops.plugins.job.PluginOperator;
import com.ezone.devops.plugins.job.enums.RegistryType;
import com.ezone.devops.plugins.job.enums.VersionType;
import com.ezone.devops.plugins.job.other.docker.model.DockerExecutorBuild;
import com.ezone.devops.plugins.job.other.docker.model.DockerExecutorConfig;
import com.ezone.devops.plugins.job.other.docker.service.DockerExecutorBuildService;
import com.ezone.devops.plugins.job.other.docker.service.DockerExecutorConfigService;
import com.ezone.devops.plugins.service.ArtifactInfoService;
import com.ezone.devops.scheduler.bean.DockerCredential;
import com.ezone.devops.scheduler.bean.ImageInfo;
import com.ezone.devops.scheduler.client.response.RunnerJobInfo;
import com.ezone.devops.scheduler.enums.ExecutorType;
import com.ezone.devops.scheduler.job.*;
import com.ezone.ezbase.iam.bean.config.DockerRegistryConfig;
import com.ezone.ezbase.iam.bean.enums.ServiceType;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.config.I18nContextHolder;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DockerExecutorOperator extends AbstractPluginOperator implements PluginOperator {

    private static final String DEFAULT_TAG = "latest";
    private static final String DOCKER_FORMAT = "%s/%s:%s";

    @Autowired
    private DockerExecutorConfigService dockerExecutorConfigService;
    @Autowired
    private DockerExecutorBuildService dockerExecutorBuildService;
    @Autowired
    private ArtifactInfoService artifactInfoService;

    @Override
    public boolean execute(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        Long companyId = repo.getCompanyId();
        DockerExecutorConfig dockerExecutorConfig = dockerExecutorConfigService.getById(jobRecord.getPluginId());
        DockerExecutorBuild dockerExecutorBuild = dockerExecutorBuildService.getById(jobRecord.getPluginRecordId());
        if (null == dockerExecutorBuild) {
            super.noticeJobFailed(pipelineRecord, jobRecord, I18nContextHolder.get("job.init.error"));
            return false;
        }

        List<DockerCredential> dockerCredentials = Lists.newArrayList();
        String image;
        DockerCredential dockerCredential = null;
        RegistryType registryType = dockerExecutorConfig.getRegistryType();
        if (registryType == RegistryType.EXTERNAL) {
            String providerName = dockerExecutorConfig.getProviderName();
            BaseResponse<Object> response = integrationService.queryDetail(companyId, ServiceType.DOCKER_REGISTRY, providerName);
            if (response == null) {
                log.info("request docker registry config error:[{}]", jobRecord);
                super.invokeDownStreamSystemFailCallback(pipelineRecord, jobRecord, "integration");
                return false;
            }
            if (response.isError()) {
                log.info("request docker registry config error:[{}],response is:[{}]", jobRecord, response);
                super.noticeJobFailed(pipelineRecord, jobRecord, response.getMessage());
                return false;
            }
            DockerRegistryConfig dockerRegistryConfig = (DockerRegistryConfig) response.getData();

            BaseResponse<Boolean> checkPermissionResponse = integrationService.checkPermission(
                    jobRecord.getTriggerUser(), companyId, ServiceType.DOCKER_REGISTRY, providerName);
            if (!checkPermissionResponse.getData()) {
                super.noticeJobFailed(pipelineRecord, jobRecord, "没有使用Docker镜像源:\"" + providerName + "\"的权限");
                return false;
            }

            image = String.format(DOCKER_FORMAT, dockerRegistryConfig.getUrl(), dockerExecutorConfig.getImageName(),
                    dockerExecutorConfig.getVersion());
            dockerCredential = new DockerCredential(dockerRegistryConfig.getUrl(),
                    dockerRegistryConfig.getUsername(), dockerRegistryConfig.getPassword());
            dockerCredentials.add(dockerCredential);
        } else {
            log.info("start request docker info:[{}]", dockerExecutorConfig);
            DockerImageInfoResponse response = ezPackageClient.getDockerImageInfo(companyId, dockerExecutorConfig);
            if (response == null) {
                log.error("request docker info error, response is null, jobRecord:[{}]", jobRecord);
                super.noticeJobFailed(pipelineRecord, jobRecord, "从制品库获取docker镜像信息失败");
                return false;
            }

            if (response.isError()) {
                log.error("get docker info error, response:[{}]", response);
                super.noticeJobFailed(pipelineRecord, jobRecord, response.getMessage());
                return false;
            }

            log.info("finished request docker info:[{}], response:[{}]", dockerExecutorConfig, response);
            DockerImageInfo dockerImageInfo = response.getData();
            image = dockerImageInfo.getImageUrl();
        }

        Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);

        ReportUploadStep reportUploadStep = null;
        if (dockerExecutorConfig.isUploadReport()) {
            String reportId = createReportInfo(pipeline);
            dockerExecutorBuild.setReportId(reportId);
            dockerExecutorBuildService.updateDockerExecutorBuild(dockerExecutorBuild);
            reportUploadStep = new ReportUploadStep()
                    .setUploadReport(true)
                    .setReportId(reportId)
                    .setReportDir(dockerExecutorConfig.getReportDir())
                    .setReportIndex(dockerExecutorConfig.getReportIndex());
        }

        FileUploadStep fileUploadStep = null;
        if (dockerExecutorConfig.isUploadArtifact()) {
            fileUploadStep = new FileUploadStep(dockerExecutorConfig, pipelineRecord.getSnapshotVersion());
        }

        DockerBuildStep dockerBuildStep = null;
        if (dockerExecutorConfig.isPushImage()) {
            DockerCredential destDockerCredential;
            String registryUrl = null;
            if (dockerExecutorConfig.getPushRegistryType() == RegistryType.EXTERNAL) {
                destDockerCredential = getDockerCredential(companyId, jobRecord, dockerExecutorConfig.getDockerRepo());
                dockerCredentials.add(destDockerCredential);
                registryUrl = destDockerCredential.getRegistryUrl();
            }

            String dockerTag = getDockerTag(envs, pipelineRecord, dockerExecutorConfig);
            if (StringUtils.isBlank(dockerTag)) {
                dockerTag = DEFAULT_TAG;
            }

            dockerExecutorBuild.setVersion(dockerTag);
            dockerExecutorBuildService.updateDockerExecutorBuild(dockerExecutorBuild);
            dockerBuildStep = new DockerBuildStep()
                    .setCredentials(dockerCredentials)
                    .setPushImage(true)
                    .setDockerfile(dockerExecutorConfig.getDockerfile())
                    .setDockerContext(dockerExecutorConfig.getDockerContext())
                    .setRegistryType(dockerExecutorConfig.getPushRegistryType())
                    .setRegistryUrl(registryUrl)
                    .setDockerRepo(dockerExecutorConfig.getDockerRepo())
                    .setDockerImageName(dockerExecutorConfig.getDockerImageName())
                    .setPlatform(dockerExecutorConfig.getPlatform())
                    .setArch(dockerExecutorConfig.getArch())
                    .setDockerTag(dockerTag);
        }

        DockerExecutorSteps dockerExecutorSteps = new DockerExecutorSteps(reportUploadStep, fileUploadStep, dockerBuildStep);
        CommonJob<DockerExecutorSteps> commonJob = new CommonJob<DockerExecutorSteps>()
                .setCloneMode(dockerExecutorConfig.getCloneMode())
                .setJobType(jobRecord.getJobType())
                .setDockerCredentials(Lists.newArrayList(dockerCredential))
                .setCommand(dockerExecutorConfig.getCommand())
                .setSteps(dockerExecutorSteps);

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

    private String getDockerTag(Map<String, String> envs, PipelineRecord pipelineRecord, DockerExecutorConfig dockerExecutorConfig) {
        if (dockerExecutorConfig.getDockerVersionType() == VersionType.CUSTOM_VERSION) {
            return FreemarkerUtil.parseExpression(dockerExecutorConfig.getDockerTag(), envs);
        } else if (dockerExecutorConfig.getDockerVersionType() == VersionType.RELEASE) {
            return pipelineRecord.getReleaseVersion();
        } else {
            return pipelineRecord.getSnapshotVersion();
        }
    }

    @Override
    public void jobSuccessCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data) {
        DockerExecutorConfig dockerExecutorConfig = dockerExecutorConfigService.getById(jobRecord.getPluginId());
        DockerExecutorBuild dockerExecutorBuild = dockerExecutorBuildService.getById(jobRecord.getPluginRecordId());

        if (dockerExecutorConfig.isUploadArtifact()) {
            artifactInfoService.saveArtifactInfo(pipelineRecord, ArtifactType.RAW, dockerExecutorConfig.getPkgRepo(),
                    dockerExecutorConfig.getPkgName(), pipelineRecord.getSnapshotVersion());
        }

        if (dockerExecutorConfig.isPushImage()) {
            artifactInfoService.saveArtifactInfo(pipelineRecord, ArtifactType.DOCKER, dockerExecutorConfig.getDockerRepo(),
                    dockerExecutorConfig.getDockerImageName(), dockerExecutorBuild.getVersion());
        }

        if (dockerExecutorConfig.isUploadReport()) {
            String reportDashBoardUrl = getReportDashBoardUrl(dockerExecutorConfig, dockerExecutorBuild);
            dockerExecutorBuild.setReportDashboardUrl(reportDashBoardUrl);
            dockerExecutorBuildService.updateDockerExecutorBuild(dockerExecutorBuild);
        }

        if (dockerExecutorConfig.isUploadReport()) {
            String reportDashBoardUrl = getReportDashBoardUrl(dockerExecutorConfig, dockerExecutorBuild);
            dockerExecutorBuild.setReportDashboardUrl(reportDashBoardUrl);
            dockerExecutorBuildService.updateDockerExecutorBuild(dockerExecutorBuild);
        }

        super.jobSuccessCallback(pipelineRecord, jobRecord, message, data);
    }


    private String getReportDashBoardUrl(DockerExecutorConfig dockerExecutorConfig,
                                         DockerExecutorBuild dockerExecutorBuild) {
        return String.format(REPORT_DASH_BOARD_URL, dockerExecutorBuild.getReportId(), dockerExecutorConfig.getReportIndex());
    }

    @Override
    public void cancel(PipelineRecord pipelineRecord, JobRecord jobRecord, String triggerUser) {
        log.info("cancel job:[{}] triggerUser:[{}]", jobRecord, triggerUser);
        super.jobCancelCallback(pipelineRecord, jobRecord, String.format("cancel job by %s", triggerUser));
        sendCancelJobLog(jobRecord, triggerUser);
        schedulerClient.cancelRunnerJob(jobRecord.getTaskId());
    }

    @Override
    public void jobStartCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message) {
    }

    @Override
    public void jobAbortCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data) {
        super.jobAbortCallback(pipelineRecord, jobRecord, message, data);
    }

}