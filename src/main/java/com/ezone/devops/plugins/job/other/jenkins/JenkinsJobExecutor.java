package com.ezone.devops.plugins.job.other.jenkins;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.enums.ResourceType;
import com.ezone.devops.pipeline.exception.CommonException;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.job.AbstractPluginOperator;
import com.ezone.devops.plugins.job.PluginOperator;
import com.ezone.devops.plugins.job.other.jenkins.bean.JenkinsCallback;
import com.ezone.devops.plugins.job.other.jenkins.model.JenkinsJobBuild;
import com.ezone.devops.plugins.job.other.jenkins.model.JenkinsJobConfig;
import com.ezone.devops.plugins.job.other.jenkins.service.JenkinsJobBuildService;
import com.ezone.devops.plugins.job.other.jenkins.service.JenkinsJobConfigService;
import com.ezone.devops.scheduler.client.response.RunnerJobResponse;
import com.ezone.devops.scheduler.job.JenkinsJob;
import com.ezone.galaxy.framework.common.bean.BaseException;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class JenkinsJobExecutor extends AbstractPluginOperator implements PluginOperator {

    @Autowired
    private JenkinsJobConfigService jenkinsJobConfigService;
    @Autowired
    private JenkinsJobBuildService jenkinsJobBuildService;

    private void validPermission(RepoVo repo, String clusterName, String triggerUser) {
        BaseResponse<?> response = schedulerClient.queryPermission(repo.getCompanyId(), ResourceType.JENKINS, clusterName, triggerUser);
        if (response.isError()) {
            throw new CommonException(response.getMessage());
        }

        Object data = response.getData();
        if (!Boolean.parseBoolean(String.valueOf(data))) {
            throw new BaseException(HttpStatus.FORBIDDEN.value(), "没有Jenkins" + clusterName + "使用的权限");
        }
    }

    @Override
    public boolean execute(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        JenkinsJobConfig jenkinsJobConfig = jenkinsJobConfigService.getById(jobRecord.getPluginId());
        validPermission(repo, jenkinsJobConfig.getJenkinsName(), jobRecord.getTriggerUser());

        Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);

        JenkinsJob jenkinsJob = new JenkinsJob();
        jenkinsJob.setCompanyId(pipeline.getCompanyId());
        jenkinsJob.setJenkinsName(jenkinsJobConfig.getJenkinsName());
        jenkinsJob.setEnvs(envs);
        jenkinsJob.setJenkinsJobName(jenkinsJobConfig.getJenkinsJobName());
        jenkinsJob.setCallbackUrl(getCallbackUrl(jobRecord));

        log.info("start create jenkins job");
        RunnerJobResponse response = schedulerClient.createJenkinsJob(jenkinsJob);
        if (response == null) {
            log.error("create jenkins job error, response is null, jobRecord:[{}]", jobRecord);
            super.noticeJobFailed(pipelineRecord, jobRecord, "创建jenkins job失败");
            return false;
        }

        if (response.isError()) {
            log.error("get docker info error, response:[{}]", response);
            super.noticeJobFailed(pipelineRecord, jobRecord, response.getMessage());
            return false;
        }

        jobRecord.setTaskId(response.getData().getTaskId());
        return true;
    }

    @Override
    public void cancel(PipelineRecord pipelineRecord, JobRecord jobRecord, String triggerUser) {
        log.info("cancel job:[{}] triggerUser:[{}]", jobRecord, triggerUser);
        schedulerClient.cancelRunnerJob(jobRecord.getTaskId());
    }

    @Override
    public void jobStartCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message) {
    }

    @Override
    public void jobSuccessCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data) {
        updateJobBuild(jobRecord, data);
        super.jobSuccessCallback(pipelineRecord, jobRecord, message, data);
    }

    @Override
    public void jobFailedCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data) {
        updateJobBuild(jobRecord, data);
        super.jobFailedCallback(pipelineRecord, jobRecord, message, data);
    }

    @Override
    public void jobAbortCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data) {
        updateJobBuild(jobRecord, data);
        super.jobAbortCallback(pipelineRecord, jobRecord, message, data);
    }

    private void updateJobBuild(JobRecord jobRecord, JSONObject data) {
        JenkinsJobBuild jenkinsJobBuild = jenkinsJobBuildService.getById(jobRecord.getPluginRecordId());
        JenkinsCallback jenkinsCallback = data.toJavaObject(JenkinsCallback.class);
        if (jenkinsCallback != null) {
            jenkinsJobBuild.setDashboardUrl(jenkinsCallback.getDashboardUrl());
            jenkinsJobBuild.setJenkinsBuildNumber(jenkinsCallback.getBuildNumber());
            jenkinsJobBuild.setJenkinsBuildStatus(jenkinsCallback.getBuildStatus());
            jenkinsJobBuild.setStartTime(jenkinsCallback.getStartTime());
            jenkinsJobBuild.setEndTime(jenkinsCallback.getEndTime());
            jenkinsJobBuildService.updateJenkinsJobBuild(jenkinsJobBuild);
        }
    }

}
