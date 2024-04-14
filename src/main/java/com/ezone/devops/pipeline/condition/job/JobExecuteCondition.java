package com.ezone.devops.pipeline.condition.job;

import com.ezone.devops.pipeline.common.BlockInfo;
import com.ezone.devops.pipeline.enums.JobConditionType;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.vo.RepoVo;

public interface JobExecuteCondition {

    JobConditionType getJobExecuteType();

    BlockInfo execute(RepoVo repoVo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord);

}
