package com.ezone.devops.plugins.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.ezcode.sdk.service.InternalRepoService;
import com.ezone.devops.ezcode.sdk.service.InternalTagService;
import com.ezone.devops.pipeline.clients.CiLogClient;
import com.ezone.devops.pipeline.clients.EzPackageClient;
import com.ezone.devops.pipeline.clients.EzPipelineClient;
import com.ezone.devops.pipeline.common.BuildStatus;
import com.ezone.devops.pipeline.config.SystemConfig;
import com.ezone.devops.pipeline.enums.ResourceType;
import com.ezone.devops.pipeline.event.service.JobRecordEventService;
import com.ezone.devops.pipeline.exception.CommonException;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.service.PipelinePermissionService;
import com.ezone.devops.pipeline.service.PipelineService;
import com.ezone.devops.pipeline.service.RepoService;
import com.ezone.devops.pipeline.service.RuntimeVariableService;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.model.BuildImage;
import com.ezone.devops.plugins.service.BuildImageService;
import com.ezone.devops.report.model.ReportInfo;
import com.ezone.devops.report.service.ReportInfoService;
import com.ezone.devops.scheduler.bean.ContainerVolume;
import com.ezone.devops.scheduler.bean.DockerCredential;
import com.ezone.devops.scheduler.bean.ImageInfo;
import com.ezone.devops.scheduler.client.SchedulerClient;
import com.ezone.devops.scheduler.client.response.RunnerJobInfo;
import com.ezone.devops.scheduler.client.response.RunnerJobResponse;
import com.ezone.devops.scheduler.enums.ExecutorType;
import com.ezone.devops.scheduler.job.CommonJob;
import com.ezone.devops.scheduler.job.RunnerJob;
import com.ezone.ezbase.iam.bean.config.DockerRegistryConfig;
import com.ezone.ezbase.iam.bean.enums.ServiceType;
import com.ezone.ezbase.iam.bean.enums.SystemType;
import com.ezone.ezbase.iam.service.IAMCenterService;
import com.ezone.ezbase.iam.service.IntegrationService;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Slf4j
public abstract class AbstractPluginOperator implements PluginOperator {

    protected static final String MAVEN_VOLUME_NAME = "maven-config";
    protected static final String NPM_VOLUME_NAME = "npm-config";
    protected static final String COMPOSER_VOLUME_NAME = "composer-config";
    protected static final String PYPI_VOLUME_NAME = "pypi-config";
    protected static final String PYPIRC_VOLUME_NAME = "pypirc-config";
    protected static final String NUGET_VOLUME_NAME = "nuget-config";
    protected static final String DEFAULT_HOME_PATH = "/root";
    protected static final String DEFAULT_MAVEN_HOME_PATH = "/root/.m2";

    protected static final String DEFAULT_MAVEN_REPO = "maven";
    protected static final String EZCODE_PLATFORM_NAME = "ezCode";

    protected static final String CANCEL_LOG = "任务被取消，取消人：%s";

    private static final String PIPELINE_CALL_BACK_URL = "%s/internal/pipelines/%s/jobs/%s";
    protected static final String REPORT_DASH_BOARD_URL = "/v1/ezpipeline/result/%s/pages/%s";

    @Autowired
    protected RuntimeVariableService runtimeVariableService;
    @Autowired
    protected RepoService repoService;
    @Autowired
    protected PipelinePermissionService pipelinePermissionService;
    @Autowired
    protected CiLogClient ciLogClient;
    @Autowired
    protected ReportInfoService reportInfoService;
    @Autowired
    protected SystemConfig systemConfig;
    @Autowired
    protected EzPackageClient ezPackageClient;
    @Autowired
    protected SchedulerClient schedulerClient;
    @Autowired
    protected EzPipelineClient ezPipelineClient;
    @Autowired
    protected IAMCenterService iamCenterService;
    @Autowired
    protected BuildImageService buildImageService;
    @Autowired
    protected IntegrationService integrationService;
    @Autowired
    protected InternalRepoService internalRepoService;
    @Autowired
    protected InternalTagService internalTagService;
    @Autowired
    private JobRecordEventService jobRecordEventService;
    @Autowired
    protected PipelineService pipelineService;

    public ContainerVolume generateMavenDefaultSetting() {
        ContainerVolume volume = new ContainerVolume();
        volume.setName(MAVEN_VOLUME_NAME);
        volume.setSource(DEFAULT_MAVEN_HOME_PATH);
        volume.setDestination(DEFAULT_MAVEN_HOME_PATH);
        return volume;
    }

    public void noticeJobSuccess(PipelineRecord pipelineRecord, JobRecord jobBuild, String message) {
        jobRecordEventService.updateJobRecordEvent(pipelineRecord, jobBuild, BuildStatus.SUCCESS, true, message);
    }

    public void noticeJobFailed(PipelineRecord pipelineRecord, JobRecord jobBuild, String message) {
        jobRecordEventService.updateJobRecordEvent(pipelineRecord, jobBuild, BuildStatus.FAIL, true, message);
    }

    public void noticeJobAbort(PipelineRecord pipelineRecord, JobRecord jobBuild, String message) {
        jobRecordEventService.updateJobRecordEvent(pipelineRecord, jobBuild, BuildStatus.ABORT, true, message);
    }

    @Override
    public void jobStartCallback(PipelineRecord pipelineRecord, JobRecord jobBuild, String message) {
    }

    @Override
    public void jobSuccessCallback(PipelineRecord pipelineRecord, JobRecord jobBuild, String message, JSONObject data) {
        noticeJobSuccess(pipelineRecord, jobBuild, message);
    }

    @Override
    public void jobFailedCallback(PipelineRecord pipelineRecord, JobRecord jobBuild, String message, JSONObject data) {
        noticeJobFailed(pipelineRecord, jobBuild, message);
    }

    @Override
    public void jobAbortCallback(PipelineRecord pipelineRecord, JobRecord jobBuild, String message, JSONObject data) {
        noticeJobAbort(pipelineRecord, jobBuild, message);
    }

    @Override
    public void jobSuccessCallback(PipelineRecord pipelineRecord, JobRecord jobBuild, String message, JSONArray data) {
        noticeJobSuccess(pipelineRecord, jobBuild, message);
    }

    @Override
    public void jobFailedCallback(PipelineRecord pipelineRecord, JobRecord jobBuild, String message, JSONArray data) {
        noticeJobFailed(pipelineRecord, jobBuild, message);
    }

    @Override
    public void jobAbortCallback(PipelineRecord pipelineRecord, JobRecord jobBuild, String message, JSONArray data) {
        noticeJobAbort(pipelineRecord, jobBuild, message);
    }

    @Override
    public void jobCancelCallback(PipelineRecord pipelineRecord, JobRecord jobBuild, String message) {
    }

    public RunnerJobInfo createRunnerJob(RepoVo repo, Pipeline pipeline, JobRecord jobBuild,
                                         ResourceType resourceType, String resourceName,
                                         ExecutorType requiredExecutorType, ImageInfo buildImage,
                                         Map<String, String> envs, CommonJob<?> commonJob) {
        commonJob.setJobType(jobBuild.getJobType());

        if (StringUtils.isBlank(resourceName)) {
            if (pipeline.isUseDefaultCluster()) {
                // 使用默认集群
                resourceName = systemConfig.getDefaultClusterName();
            }
        }

        RunnerJob payload = new RunnerJob()
                .setSystemType(SystemType.EZPIPELINE)
                .setCompanyId(repo.getCompanyId())
                .setResourceType(resourceType)
                .setResourceName(resourceName)
                .setUsername(jobBuild.getTriggerUser())
                .setRequiredExecutorType(requiredExecutorType)
                .setJobTimeoutMinute(pipeline.getJobTimeoutMinute())
                .setBuildImage(buildImage).setEnvs(envs).setJobDetail(commonJob)
                .setCallbackUrl(getCallbackUrl(jobBuild));

        log.info("start create runner job [{}]", jobBuild.getId());
        RunnerJobResponse runnerJobResponse = schedulerClient.createRunnerJob(payload);

        if (runnerJobResponse == null) {
            log.error("create runner job [{}] error, response is null", jobBuild.getId());
            throw new CommonException("创建任务失败");
        }

        if (runnerJobResponse.isError()) {
            log.error("create runner job [{}] error, message:[{}]", jobBuild.getId(), runnerJobResponse.getMessage());
            throw new CommonException(runnerJobResponse.getMessage());
        }

        log.info("create runner job [{}] success", jobBuild.getId());
        return runnerJobResponse.getData();
    }

    protected String getCallbackUrl(JobRecord jobRecord) {
        return String.format(PIPELINE_CALL_BACK_URL, ezPipelineClient.getEndpoint(),
                jobRecord.getPipelineId(), jobRecord.getExternalJobId());
    }

    protected String getBuildImageUrl(Long buildImageId) {
        BuildImage buildImage = buildImageService.getByIdIfPresent(buildImageId);
        String prefix = systemConfig.getBuildImagePrefix();
        if (StringUtils.isBlank(prefix)) {
            return buildImage.getImage() + ":" + buildImage.getTag();
        } else {
            return prefix + "/" + buildImage.getImage() + ":" + buildImage.getTag();
        }
    }

    protected void sendCancelJobLog(JobRecord jobBuild, String triggerUser) {
        ciLogClient.sendLnLog(jobBuild.getLogName(), CiLogClient.LogLevel.WARN, String.format(CANCEL_LOG, triggerUser));
    }

    public String createReportInfo(Pipeline pipeline) {
        ReportInfo reportInfo = reportInfoService.createReportInfo(pipeline);
        return reportInfo.getReportId();
    }


    public DockerCredential getDockerCredential(Long companyId, JobRecord jobBuild, String providerName) {
        log.info("start request docker credential from ezbase, plugin:[{}], providerName:[{}]", jobBuild.getJobType(),
                providerName);
        BaseResponse<Object> response = integrationService.queryDetail(companyId, ServiceType.DOCKER_REGISTRY, providerName);
        if (response == null) {
            log.info("request docker credential config error:[{}]", jobBuild);
            throw new CommonException("invoke ezbase error");
        }
        if (response.isError()) {
            log.info("request docker credential config error:[{}],response is:[{}]", jobBuild, response);
            throw new CommonException(response.getMessage());
        }
        DockerRegistryConfig dockerRegistryConfig = (DockerRegistryConfig) response.getData();
        BaseResponse<Boolean> checkPermissionResponse = integrationService.checkPermission(
                jobBuild.getTriggerUser(), companyId, ServiceType.DOCKER_REGISTRY, providerName);
        if (!checkPermissionResponse.getData()) {
            throw new CommonException("没有使用Docker镜像源:\"" + providerName + "\"的权限");
        }
        DockerCredential dockerCredential = new DockerCredential(dockerRegistryConfig.getUrl(),
                dockerRegistryConfig.getUsername(), dockerRegistryConfig.getPassword());
        log.info("finished request docker credential from ezbase, plugin:[{}], providerName:[{}]",
                jobBuild.getJobType(), providerName);
        return dockerCredential;
    }

    public void invokeDownStreamSystemFailCallback(PipelineRecord pipelineRecord, JobRecord jobBuild, String who) {
        jobRecordEventService.updateJobRecordEvent(pipelineRecord, jobBuild, BuildStatus.FAIL, true,
                "调用插件平台" + who + "超时或出现异常");
    }

}
