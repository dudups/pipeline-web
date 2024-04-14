package com.ezone.devops.plugins.job.other.jenkins.dao;

import com.ezone.devops.plugins.job.other.jenkins.model.JenkinsJobConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface JenkinsJobConfigDao extends LongKeyBaseDao<JenkinsJobConfig> {

    String ID = "id";
    String JENKINS_NAME = "jenkins_name";
    String JENKINS_JOB_NAME = "jenkins_job_name";
}
