package com.ezone.devops.plugins.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.common.BuildStatus;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.galaxy.framework.main.concurrent.aop.Pooled;

public interface PluginOperator {

    boolean execute(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord);

    @Pooled(timeout = 20000)
    default void asyncCancel(PipelineRecord pipelineRecord, JobRecord jobRecord, String triggerUser) {
        if (BuildStatus.isEnd(jobRecord.getStatus())) {
            return;
        }
        cancel(pipelineRecord, jobRecord, triggerUser);
    }

    default void cancel(PipelineRecord pipelineRecord, JobRecord jobRecord, String triggerUser) {
    }

    @Pooled(timeout = 20000)
    void jobStartCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message);

    void jobSuccessCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data);

    void jobFailedCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data);

    void jobAbortCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data);

    void jobSuccessCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONArray data);

    void jobFailedCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONArray data);

    void jobAbortCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONArray data);

    void jobCancelCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message);
}
