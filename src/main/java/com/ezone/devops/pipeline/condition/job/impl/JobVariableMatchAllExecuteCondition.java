package com.ezone.devops.pipeline.condition.job.impl;

import com.ezone.devops.pipeline.common.BlockInfo;
import com.ezone.devops.pipeline.enums.JobConditionType;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.vo.RepoVo;
import org.springframework.stereotype.Component;

@Component
public class JobVariableMatchAllExecuteCondition extends JobAbstractExecuteCondition {

    @Override
    public JobConditionType getJobExecuteType() {
        return JobConditionType.VARIABLE_MATCH_ALL;
    }

    @Override
    public BlockInfo execute(RepoVo repoVo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord) {

        return assembleNoBlockReason();
    }

}
