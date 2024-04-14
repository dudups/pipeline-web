package com.ezone.devops.pipeline.model;

import com.ezone.devops.pipeline.dao.JobDao;
import com.ezone.devops.pipeline.enums.ConditionTriggerType;
import com.ezone.devops.pipeline.enums.JobConditionType;
import com.ezone.devops.pipeline.web.request.JobPayload;
import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "job")
@NoArgsConstructor
public class Job extends LongID {

    public static final Long HEAD_JOB_ID = 0L;

    @Column(JobDao.ID)
    private Long id;
    @Column(JobDao.NAME)
    private String name;
    @Column(JobDao.CONDITION_TYPE)
    private JobConditionType conditionType;
    @Column(JobDao.CONDITION_TRIGGER_TYPE)
    private ConditionTriggerType conditionTriggerType;
    @Column(JobDao.JOB_TYPE)
    private String jobType;
    @Column(JobDao.PLUGIN_TYPE)
    private PluginType pluginType;
    @Column(JobDao.STAGE_ID)
    private Long stageId;
    @Column(JobDao.PLUGIN_ID)
    private Long pluginId;
    @Column(JobDao.UPSTREAM_ID)
    private Long upstreamId;

    public Job(Stage stage, JobPayload jobPayload, Long upstreamId) {
        setUpstreamId(upstreamId);
        setStageId(stage.getId());

        setConditionType(jobPayload.getConditionType());
        setConditionTriggerType(jobPayload.getConditionTriggerType());

        setPluginType(jobPayload.getPluginType());
        setName(jobPayload.getJobName());
        setJobType(jobPayload.getJobType());
    }
}
