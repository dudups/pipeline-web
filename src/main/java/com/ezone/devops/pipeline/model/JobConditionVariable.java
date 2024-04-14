package com.ezone.devops.pipeline.model;

import com.ezone.devops.pipeline.dao.JobConditionVariableDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "job_condition_variable", shardCount = 10)
public class JobConditionVariable extends LongID {

    @Column(JobConditionVariableDao.ID)
    private Long id;
    @Column(JobConditionVariableDao.PIPELINE_ID)
    private Long pipelineId;
    @Column(JobConditionVariableDao.JOB_ID)
    private Long jobId;
    @Column(JobConditionVariableDao.ENV_KEY)
    private String envKey;
    @Column(JobConditionVariableDao.ENV_VALUE)
    private String envValue;

}


