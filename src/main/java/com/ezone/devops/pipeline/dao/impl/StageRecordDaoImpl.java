package com.ezone.devops.pipeline.dao.impl;

import com.ezone.devops.pipeline.dao.StageRecordDao;
import com.ezone.devops.pipeline.model.StageRecord;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import com.ezone.galaxy.fasterdao.operator.Match;
import com.ezone.galaxy.fasterdao.param.NotParam;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class StageRecordDaoImpl extends BaseCommonDao<StageRecord> implements StageRecordDao {

    @Override
    public List<StageRecord> getByPipelineBuildId(Long pipelineRecordId) {
        return find(match(PIPELINE_RECORD_ID, pipelineRecordId), order(UPSTREAM_ID, true));
    }

    @Override
    public List<StageRecord> getByPipelineBuildIds(Collection<Long> pipelineBuildIds) {
        return find(match(PIPELINE_RECORD_ID, pipelineBuildIds));
    }

    @Override
    public StageRecord getFirstStageByPipelineBuildId(Long pipelineBuildId) {
        List<Match> matches = Lists.newArrayList();
        matches.add(match(PIPELINE_RECORD_ID, pipelineBuildId));
        matches.add(match(UPSTREAM_ID, StageRecord.HEAD_STAGE_ID));
        return findOne(matches);
    }

    @Override
    public List<StageRecord> getParentStageRecords(Long pipelineRecordId, Long stageBuildId) {
        return find(match(PIPELINE_RECORD_ID, pipelineRecordId), match(UPSTREAM_ID, lessThan(stageBuildId)));
    }

    @Override
    public int deleteNotExistByPipelineIds(Collection<Long> pipelineIds) {
        return delete(match(PIPELINE_ID, new NotParam(pipelineIds)));
    }

    @Override
    public void deleteAll() {
        delete();
    }

    @Override
    public void deleteByPipelineId(Long pipelineId) {
        delete(match(PIPELINE_ID, pipelineId));
    }

    @Override
    public void deleteByPipelineRecordId(Long pipelineRecordId) {
        delete(match(PIPELINE_RECORD_ID, pipelineRecordId));
    }

}
