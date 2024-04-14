package com.ezone.devops.pipeline.event.service;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.common.BuildStatus;
import com.ezone.devops.pipeline.enums.OperationType;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.PipelineRecord;

/**
 * 事件驱动执行job记录的服务接口
 */
public interface JobRecordEventService {

    /**
     * 开始执行job
     */
    void startJobRecordEvent(PipelineRecord pipelineRecord, JobRecord jobRecord, String triggerUser, JSONObject jobParams,
                             boolean forceStart);

    /**
     * 取消执行job
     */
    void cancelJobRecordEvent(PipelineRecord pipelineRecord, JobRecord jobRecord, String triggerUser);

    /**
     * 跳过执行job
     */
    void skipJobRecordEvent(PipelineRecord pipelineRecord, JobRecord jobRecord, String triggerUser);

    /**
     * 跳过执行子流水线的所有job
     */
    void skipAllJobRecordEvent(PipelineRecord pipelineRecord, JobRecord headJobRecord, String triggerUser);

    /**
     * 更新job的事件
     */
    void updateJobRecordEvent(PipelineRecord pipelineRecord, JobRecord jobRecord, BuildStatus status, boolean appendMessage, String message);

    /**
     * job的开始执行，重新执行，取消执行
     *
     * @param jobRecord
     * @param jobParams
     */
    void jobOperator(JobRecord jobRecord, String triggerUser, OperationType operationType, JSONObject jobParams);

}
