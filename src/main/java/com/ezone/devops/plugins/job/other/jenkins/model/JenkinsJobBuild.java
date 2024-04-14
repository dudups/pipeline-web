package com.ezone.devops.plugins.job.other.jenkins.model;


import com.ezone.devops.plugins.annotation.ManualField;
import com.ezone.devops.plugins.job.other.jenkins.dao.JenkinsJobBuildDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;


@Data
@Table(name = "plugin_jenkins_job_build")
public class JenkinsJobBuild extends LongID {
    @Column(JenkinsJobBuildDao.ID)
    private Long id;
    @ManualField
    @Column(JenkinsJobBuildDao.DASHBOARD_URL)
    private String dashboardUrl;
    @ManualField
    @Column(JenkinsJobBuildDao.JENKINS_BUILD_NUMBER)
    private Long jenkinsBuildNumber;
    @ManualField
    @Column(JenkinsJobBuildDao.JENKINS_BUILD_STATUS)
    private String jenkinsBuildStatus;
    @ManualField
    @Column(JenkinsJobBuildDao.START_TIME)
    private Long startTime;
    @ManualField
    @Column(JenkinsJobBuildDao.END_TIME)
    private Long endTime;
}
