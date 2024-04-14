package com.ezone.devops.pipeline.dao.impl;

import com.ezone.devops.pipeline.dao.JobDao;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import com.ezone.galaxy.fasterdao.param.NotParam;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class JobDaoImpl extends BaseCommonDao<Job> implements JobDao {

    @Override
    public List<Job> getJobsByStageId(Long stageId) {
        return find(match(STAGE_ID, stageId));
    }

    @Override
    public List<Job> getJobsByStageIds(Collection<Long> stageIds) {
        return find(match(STAGE_ID, stageIds));
    }

    @Override
    public List<Job> getByJobType(String jobType) {
        return find(match(JOB_TYPE, jobType));
    }

    @Override
    public void deleteAll() {
        delete();
    }

    @Override
    public int deleteNotExistByStageIds(Collection<Long> stageIds) {
        return delete(match(STAGE_ID, new NotParam(stageIds)));
    }

}
