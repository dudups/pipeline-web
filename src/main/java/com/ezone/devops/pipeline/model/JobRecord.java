package com.ezone.devops.pipeline.model;

import com.ezone.devops.pipeline.common.BuildStatus;
import com.ezone.devops.pipeline.dao.JobRecordDao;
import com.ezone.devops.pipeline.enums.ConditionTriggerType;
import com.ezone.devops.pipeline.enums.JobConditionType;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import com.ezone.galaxy.framework.common.util.SnowFlakeGenerator;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@Data
@NoArgsConstructor
@Table(name = "job_record")
public class JobRecord extends LongID {

    public static final Long HEAD_JOB_RECORD_ID = 0L;
    public static final Long DEFAULT_TASK_ID = 0L;

    @Column(JobRecordDao.ID)
    private Long id;
    // 给外部使用的jobId，为了解决重新执行job变化的问题
    @Column(JobRecordDao.EXTERNAL_JOB_ID)
    private Long externalJobId;
    @Column(JobRecordDao.REPO_KEY)
    private String repoKey;
    @Column(value = JobRecordDao.PIPELINE_ID, maybeModified = false)
    private Long pipelineId;
    @Column(value = JobRecordDao.JOB_ID, maybeModified = false)
    private Long jobId;
    @Column(value = JobRecordDao.PIPELINE_RECORD_ID, maybeModified = false)
    private Long pipelineRecordId;
    @Column(value = JobRecordDao.STAGE_RECORD_ID, maybeModified = false)
    private Long stageRecordId;
    @Column(JobRecordDao.PLUGIN_ID)
    private Long pluginId;
    @Column(JobRecordDao.PLUGIN_RECORD_ID)
    private Long pluginRecordId;
    @Column(JobRecordDao.UPSTREAM_ID)
    private Long upstreamId;
    @Column(JobRecordDao.NAME)
    private String name;
    @Column(JobRecordDao.JOB_TYPE)
    private String jobType;
    @Column(JobRecordDao.PLUGIN_TYPE)
    private PluginType pluginType;
    @Column(JobRecordDao.TASK_ID)
    private Long taskId;
    @Column(JobRecordDao.LOG_NAME)
    private String logName;
    @Column(JobRecordDao.STATUS)
    private BuildStatus status;
    @Column(JobRecordDao.TRIGGER_USER)
    private String triggerUser;
    @Column(JobRecordDao.MESSAGE)
    private String message;
    @Column(JobRecordDao.CREATE_TIME)
    private Date createTime;
    @Column(JobRecordDao.MODIFY_TIME)
    private Date modifyTime;

    @Column(JobRecordDao.CONDITION_TYPE)
    private JobConditionType conditionType;
    @Column(JobRecordDao.CONDITION_TRIGGER_TYPE)
    private ConditionTriggerType conditionTriggerType;

    public JobRecord(RepoVo repo, StageRecord stageRecord, Job job, Long upstreamId) {
        setExternalJobId(SnowFlakeGenerator.getSnowflakeId());
        setStatus(BuildStatus.PENDING);

        setName(job.getName());
        setJobType(job.getJobType());
        setPluginId(job.getPluginId());

        setRepoKey(repo.getRepoKey());
        setPipelineId(stageRecord.getPipelineId());
        setPipelineRecordId(stageRecord.getPipelineRecordId());
        setStageRecordId(stageRecord.getId());
        setJobId(job.getId());
        setUpstreamId(upstreamId);
        setPluginType(job.getPluginType());

        setLogName(StringUtils.EMPTY);
        setMessage(StringUtils.EMPTY);
        setPluginRecordId(HEAD_JOB_RECORD_ID);
        setTaskId(DEFAULT_TASK_ID);

        setTriggerUser(StringUtils.EMPTY);

        setConditionType(job.getConditionType());
        setConditionTriggerType(job.getConditionTriggerType());
    }
}
