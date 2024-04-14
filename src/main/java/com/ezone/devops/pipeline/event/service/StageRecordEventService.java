package com.ezone.devops.pipeline.event.service;

import com.ezone.devops.pipeline.enums.OperationType;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.model.StageRecord;

/**
 * 事件驱动执行stage记录的服务接口
 */
public interface StageRecordEventService {

    /**
     * 开始执行stage
     */
    void startBuildRecordEvent(PipelineRecord pipelineRecord, StageRecord stageRecord, String triggerUser);

    /**
     * 取消执行stage
     */
    void cancelBuildRecordEvent(PipelineRecord pipelineRecord, StageRecord stageRecord, String triggerUser);

    /**
     * 跳过执行stage
     */
    void skipBuildRecordEvent(PipelineRecord pipelineRecord, StageRecord stageRecord, String triggerUser);

    /**
     * stage的开始执行，取消执行，跳过执行
     */
    void stageOperator(PipelineRecord pipelineRecord, StageRecord stageRecord, String triggerUser, OperationType operationType);

    /**
     * 更新stage事件
     */
    void updateStageRecordEvent(Long stageBuildId);

}
