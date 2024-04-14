package com.ezone.devops.pipeline.dao.impl;

import com.ezone.devops.pipeline.common.BuildStatus;
import com.ezone.devops.pipeline.dao.JobRecordDao;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import com.ezone.galaxy.fasterdao.operator.Match;
import com.ezone.galaxy.fasterdao.param.NotParam;
import com.ezone.galaxy.fasterdao.param.Params;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Repository
public class JobRecordDaoImpl extends BaseCommonDao<JobRecord> implements JobRecordDao {

    @Override
    public JobRecord getByExternalJobId(Long externalJobId) {
        return findOne(match(EXTERNAL_JOB_ID, externalJobId));
    }

    @Override
    public List<JobRecord> getByPipelineAndPipelineRecordId(Long pipelineId, Long pipelineRecordId) {
        return find(match(PIPELINE_ID, pipelineId), match(PIPELINE_RECORD_ID, pipelineRecordId));
    }

    @Override
    public List<JobRecord> getJobRecordsByStageRecordId(Long stageRecordId) {
        return find(match(STAGE_RECORD_ID, stageRecordId));
    }

    @Override
    public JobRecord getNextJob(Long stageBuildId, Long upstreamId) {
        List<Match> matches = Lists.newArrayList();
        matches.add(match(STAGE_RECORD_ID, stageBuildId));
        matches.add(match(UPSTREAM_ID, upstreamId));
        return findOne(matches);
    }

    @Override
    public List<JobRecord> getMiniPipelineHeadJobRecords(Long stageBuildId, Long upstreamId) {
        List<Match> matches = Lists.newArrayList();
        matches.add(match(STAGE_RECORD_ID, stageBuildId));
        matches.add(match(UPSTREAM_ID, upstreamId));
        return find(matches);
    }

    @Override
    public List<JobRecord> getByStatus(String repoKey, Collection<BuildStatus> buildStatuses, Date start, Date end) {
        return find(match(REPO_KEY, repoKey), match(STATUS, buildStatuses), match(MODIFY_TIME, Params.between(start, end)));
    }

    @Override
    public List<JobRecord> getByJobType(String jobType) {
        return find(match(JOB_TYPE, jobType));
    }

    @Override
    public void deleteAll() {
        delete();
    }

    @Override
    public int deleteNotExistByPipelineIds(Collection<Long> pipelineIds) {
        return delete(match(PIPELINE_ID, new NotParam(pipelineIds)));
    }

    @Override
    public void deleteByRepoKey(String repoKey) {
        delete(match(REPO_KEY, repoKey));
    }

    @Override
    public void deleteByPipelineRecordId(Long pipelineRecordId) {
        delete(match(PIPELINE_RECORD_ID, pipelineRecordId));
    }
}
