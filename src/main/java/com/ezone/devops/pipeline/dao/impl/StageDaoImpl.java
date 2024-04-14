package com.ezone.devops.pipeline.dao.impl;

import com.ezone.devops.pipeline.dao.StageDao;
import com.ezone.devops.pipeline.model.Stage;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import com.ezone.galaxy.fasterdao.param.NotParam;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class StageDaoImpl extends BaseCommonDao<Stage> implements StageDao {

    @Override
    public List<Stage> getByPipelineId(Long pipelineId) {
        return find(match(PIPELINE_ID, pipelineId));
    }

    @Override
    public List<Stage> getByPipelineIds(Collection<Long> pipelineIds) {
        return find(match(PIPELINE_ID, pipelineIds));
    }

    @Override
    public int deleteNotExistByPipelineIds(Collection<Long> pipelineIds) {
        return delete(match(PIPELINE_ID, new NotParam(pipelineIds)));
    }

    @Override
    public void deleteAll() {
        delete();
    }

}
