package com.ezone.devops.pipeline.dao;

import com.ezone.devops.pipeline.common.BuildStatus;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface JobRecordDao extends LongKeyBaseDao<JobRecord> {

    String ID = "id";
    String EXTERNAL_JOB_ID = "external_job_id";
    String REPO_KEY = "repo_key";
    String PIPELINE_ID = "pipeline_id";
    String JOB_ID = "job_id";
    String PIPELINE_RECORD_ID = "pipeline_record_id";
    String STAGE_RECORD_ID = "stage_record_id";
    String PLUGIN_ID = "plugin_id";
    String PLUGIN_RECORD_ID = "plugin_record_id";
    String UPSTREAM_ID = "upstream_id";
    String NAME = "name";
    String JOB_TYPE = "job_type";
    String PLUGIN_TYPE = "plugin_type";
    String TASK_ID = "task_id";
    String LOG_NAME = "log_name";
    String STATUS = "status";
    String TRIGGER_USER = "trigger_user";
    String MESSAGE = "message";
    String CREATE_TIME = "create_time";
    String MODIFY_TIME = "modify_time";
    String CONDITION_TYPE = "condition_type";
    String CONDITION_TRIGGER_TYPE = "condition_trigger_type";

    JobRecord getByExternalJobId(Long externalJobId);

    List<JobRecord> getByPipelineAndPipelineRecordId(Long pipelineId, Long pipelineRecordId);

    List<JobRecord> getJobRecordsByStageRecordId(Long stageRecordId);

    JobRecord getNextJob(Long stageBuildId, Long upstreamId);

    List<JobRecord> getMiniPipelineHeadJobRecords(Long stageBuildId, Long upstreamId);

    List<JobRecord> getByStatus(String repoKey, Collection<BuildStatus> buildStatuses, Date start, Date end);

    List<JobRecord> getByJobType(String jobType);

    void deleteAll();

    int deleteNotExistByPipelineIds(Collection<Long> pipelineIds);

    void deleteByRepoKey(String repoKey);

    void deleteByPipelineRecordId(Long pipelineRecordId);
}
