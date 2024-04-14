package com.ezone.devops.pipeline.service;

import com.ezone.devops.pipeline.common.BlockInfo;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.vo.RepoVo;

public interface ConditionService {

    BlockInfo blockJobExecute(RepoVo repoVo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord);

}
