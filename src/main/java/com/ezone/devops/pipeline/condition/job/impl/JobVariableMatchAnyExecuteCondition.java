package com.ezone.devops.pipeline.condition.job.impl;

import com.ezone.devops.pipeline.common.BlockInfo;
import com.ezone.devops.pipeline.enums.JobConditionType;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.vo.RepoVo;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JobVariableMatchAnyExecuteCondition extends JobAbstractExecuteCondition {

    @Override
    public JobConditionType getJobExecuteType() {
        return JobConditionType.VARIABLE_MATCH_ANY;
    }

    @Override
    public BlockInfo execute(RepoVo repoVo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        Map<String, String> allRuntimeVariables = runtimeVariableService.getAllVariables(repoVo, pipeline, pipelineRecord, jobRecord);
        Map<String, String> conditionVariables = jobConditionVariableService.getJobConditionVariables(pipeline, jobRecord);
        if (MapUtils.isEmpty(conditionVariables)) {
            return assembleNoBlockReason();
        }

        for (Map.Entry<String, String> entry : conditionVariables.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String runtimeValue = allRuntimeVariables.get(key);
            if (StringUtils.equals(value, runtimeValue)) {
                return assembleBlockReason(jobRecord, key, value);
            }
        }

        return assembleNoBlockReason();
    }

}
