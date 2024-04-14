package com.ezone.devops.pipeline.service;

import com.ezone.devops.pipeline.common.BuildStatus;
import com.ezone.devops.pipeline.model.*;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.pipeline.web.response.JobRecordVo;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface JobRecordService {

    JobRecord getByIdIfPresent(Long id);

    JobRecord getByExternalJobIdIfPresent(Long externalJobId);

    void initJobRecords(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, StageRecord stageRecord, Stage stage);

    JobRecord reInitJobRecord(RepoVo repo, PipelineRecord pipelineRecord, JobRecord jobBuild);

    JobRecord updateJobRecord(JobRecord jobBuild);

    List<JobRecord> getJobRecords(StageRecord stageRecord);

    Map<Long, List<List<JobRecordVo>>> getJobRecordGroups(Pipeline pipeline, PipelineRecord pipelineRecord);

    JobRecord getRunningJob(JobRecord jobBuild);

    JobRecord getNextWaitingJob(JobRecord jobBuild);

    List<JobRecord> getMiniPipelineHeadJobRecords(StageRecord stageRecord);

    List<JobRecord> getMiniPipelineByHeadJob(JobRecord headJobBuild);

    List<JobRecord> getByStatus(String repoKey, Collection<BuildStatus> buildStatuses, Date start, Date end);

    void deleteByRepo(RepoVo repo);

    void deleteByPipelineRecord(PipelineRecord pipelineRecord);
}

