package com.ezone.devops.plugins.job.other.jenkins.model;

import com.ezone.devops.plugins.job.other.jenkins.dao.JenkinsJobConfigDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;

@Data
@Table(name = "plugin_jenkins_job_config")
public class JenkinsJobConfig extends LongID {

    @Column(JenkinsJobConfigDao.ID)
    private Long id;
    @Column(JenkinsJobConfigDao.JENKINS_NAME)
    private String jenkinsName;
    @Column(JenkinsJobConfigDao.JENKINS_JOB_NAME)
    private String jenkinsJobName;

}
