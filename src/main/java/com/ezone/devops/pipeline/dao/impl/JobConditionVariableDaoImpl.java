package com.ezone.devops.pipeline.dao.impl;

import com.ezone.devops.pipeline.dao.JobConditionVariableDao;
import com.ezone.devops.pipeline.model.JobConditionVariable;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JobConditionVariableDaoImpl extends BaseCommonDao<JobConditionVariable> implements JobConditionVariableDao {

    @Override
    public List<JobConditionVariable> getByJobId(Long jobId) {
        return find(match(JOB_ID, jobId));
    }

    @Override
    public int deleteByPipelineId(Long pipelineId) {
        return delete(match(PIPELINE_ID, pipelineId));
    }
}
