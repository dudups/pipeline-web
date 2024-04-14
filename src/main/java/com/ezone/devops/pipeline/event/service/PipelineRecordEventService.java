package com.ezone.devops.pipeline.event.service;

import com.ezone.devops.pipeline.model.PipelineRecord;

/**
 * 流水线的执行逻辑接口
 */
public interface PipelineRecordEventService {

    /**
     * 开始执行流水线
     * 1,判断流水线是否应该被阻塞
     * 2,执行第一个waiting stage
     */
    boolean startPipelineRecordEvent(PipelineRecord pipelineRecord, String triggerUser);

    /**
     * 取消执行流水线
     */
    void cancelPipelineRecordEvent(PipelineRecord pipelineRecord, String triggerUser);

    /**
     * 更新流水线状态,更新流水线开始结束时间
     *
     * @param pipelineRecord 构建记录
     */
    void updatePipelineRecordEvent(PipelineRecord pipelineRecord);

}
