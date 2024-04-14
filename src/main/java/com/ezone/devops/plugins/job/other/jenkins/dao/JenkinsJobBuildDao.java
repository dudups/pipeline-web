package com.ezone.devops.plugins.job.other.jenkins.dao;


import com.ezone.devops.plugins.job.other.jenkins.model.JenkinsJobBuild;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface JenkinsJobBuildDao extends LongKeyBaseDao<JenkinsJobBuild> {
    String ID = "id";
    String DASHBOARD_URL = "dashboard_url";
    String JENKINS_BUILD_NUMBER = "jenkins_build_number";
    String JENKINS_BUILD_STATUS = "jenkins_build_status";
    String START_TIME = "start_time";
    String END_TIME = "end_time";
}
