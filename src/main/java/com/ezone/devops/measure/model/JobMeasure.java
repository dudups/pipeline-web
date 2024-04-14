package com.ezone.devops.measure.model;

import com.ezone.devops.measure.dao.JobMeasureDao;
import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Table(name = "job_measure")
@NoArgsConstructor
public class JobMeasure extends LongID {

    @Column(JobMeasureDao.ID)
    private Long id;
    @Column(JobMeasureDao.REPO_KEY)
    private String repoKey;
    @Column(JobMeasureDao.JOB_TYPE)
    private String jobType;
    @Column(JobMeasureDao.PLUGIN_TYPE)
    private PluginType pluginType;
    @Column(JobMeasureDao.START_TIME)
    private Date startTime;
    @Column(JobMeasureDao.END_TIME)
    private Date endTime;
    @Column(JobMeasureDao.SUCCESS)
    private boolean success;
    @Column(JobMeasureDao.COST_TIME)
    private long costTime;
}
