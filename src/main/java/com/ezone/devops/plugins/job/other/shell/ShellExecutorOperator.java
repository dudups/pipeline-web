package com.ezone.devops.plugins.job.other.shell;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.job.AbstractPluginOperator;
import com.ezone.devops.plugins.job.PluginOperator;
import com.ezone.devops.plugins.job.other.shell.model.ShellExecutorConfig;
import com.ezone.devops.scheduler.bean.ImageInfo;
import com.ezone.devops.scheduler.client.response.RunnerJobInfo;
import com.ezone.devops.scheduler.enums.ExecutorType;
import com.ezone.devops.scheduler.job.CommonJob;
import com.ezone.devops.scheduler.job.EmptyStep;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class ShellExecutorOperator extends AbstractPluginOperator implements PluginOperator {

    @Autowired
    private ShellExecutorDataOperator shellExecutorDataOperator;

    @Override
    public boolean execute(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        ShellExecutorConfig shellExecutorConfig = shellExecutorDataOperator.getRealJob(jobRecord.getPluginId());

        Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);

        String image = getBuildImageUrl(shellExecutorConfig.getBuildImageId());

        CommonJob<EmptyStep> commonJob = new CommonJob<EmptyStep>()
                .setCloneMode(shellExecutorConfig.getCloneMode())
                .setJobType(jobRecord.getJobType())
                .setCommand(shellExecutorConfig.getCommand());

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
        log.info("cancel job:[{}] triggerUser:[{}]", jobRecord, triggerUser);
        sendCancelJobLog(jobRecord, triggerUser);
        schedulerClient.cancelRunnerJob(jobRecord.getTaskId());
    }

    @Override
    public void jobStartCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message) {
    }

    @Override
    public void jobSuccessCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data) {
        super.jobSuccessCallback(pipelineRecord, jobRecord, message, data);
    }

    @Override
    public void jobFailedCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data) {
        super.jobFailedCallback(pipelineRecord, jobRecord, message, data);
    }

    @Override
    public void jobAbortCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data) {
        super.jobAbortCallback(pipelineRecord, jobRecord, message, data);
    }

}