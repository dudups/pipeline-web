package com.ezone.devops.pipeline.dao;

import com.ezone.devops.pipeline.model.JobConditionVariable;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

import java.util.List;

public interface JobConditionVariableDao extends LongKeyBaseDao<JobConditionVariable> {

    String ID = "id";
    String PIPELINE_ID = "pipeline_id";
    String JOB_ID = "job_id";
    String ENV_KEY = "env_key";
    String ENV_VALUE = "env_value";

    List<JobConditionVariable> getByJobId(Long jobId);

    int deleteByPipelineId(Long pipelineId);

}
