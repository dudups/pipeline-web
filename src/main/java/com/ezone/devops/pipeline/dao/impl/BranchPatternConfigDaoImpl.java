package com.ezone.devops.pipeline.dao.impl;

import com.ezone.devops.pipeline.dao.BranchPatternConfigDao;
import com.ezone.devops.pipeline.model.BranchPatternConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import com.ezone.galaxy.fasterdao.param.NotParam;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class BranchPatternConfigDaoImpl extends BaseCommonDao<BranchPatternConfig> implements
        BranchPatternConfigDao {

    @Override
    public List<BranchPatternConfig> getByPipelineIds(Collection<Long> pipelineIds) {
        return find(match(PIPELINE_ID, pipelineIds));
    }

    @Override
    public BranchPatternConfig getByPipelineId(Long pipelineId) {
        return findOne(match(PIPELINE_ID, pipelineId));
    }

    @Override
    public boolean deleteBranchPatternByPipelineId(Long pipelineId) {
        return delete(match(PIPELINE_ID, pipelineId)) > 0;
    }

    @Override
    public void deleteAll() {
        delete();
    }

    @Override
    public int deleteNotExistByPipelineIds(Collection<Long> pipelineIds) {
        return delete(match(PIPELINE_ID, new NotParam(pipelineIds)));
    }
}
