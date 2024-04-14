package com.ezone.devops.pipeline.condition.job.impl;

import com.ezone.devops.pipeline.common.BlockInfo;
import com.ezone.devops.pipeline.condition.job.JobExecuteCondition;
import com.ezone.devops.pipeline.enums.ConditionTriggerType;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.service.JobConditionVariableService;
import com.ezone.devops.pipeline.service.RuntimeVariableService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class JobAbstractExecuteCondition implements JobExecuteCondition {

    private static final String SKIP_REASON_FORMAT = "程序控制：由变量%s=%s触发自动跳过此任务，任务跳过后将无法再执行";
    private static final String STOP_REASON_FORMAT = "程序控制：由变量%s=%s触发自动停止此任务";

    @Autowired
    protected JobConditionVariableService jobConditionVariableService;
    @Autowired
    protected RuntimeVariableService runtimeVariableService;

    protected BlockInfo assembleBlockReason(JobRecord jobRecord, String key, String value) {
        BlockInfo blockInfo = new BlockInfo();
        blockInfo.setBlocked(true);
        if (jobRecord.getConditionTriggerType() == ConditionTriggerType.SKIP) {
            blockInfo.setReason(String.format(SKIP_REASON_FORMAT, key, value));
        } else {
            blockInfo.setReason(String.format(STOP_REASON_FORMAT, key, value));
        }
        return blockInfo;
    }

    protected BlockInfo assembleNoBlockReason() {
        BlockInfo blockInfo = new BlockInfo();
        blockInfo.setBlocked(false);
        return blockInfo;
    }

}
