package com.ezone.devops.plugins.job.other.jenkins.service;

import com.ezone.devops.plugins.job.other.jenkins.model.JenkinsJobConfig;

public interface JenkinsJobConfigService {

    boolean deleteJenkinsJobConfig(Long id);

    JenkinsJobConfig getById(Long id);

    JenkinsJobConfig saveJenkinsJobConfig(JenkinsJobConfig jenkinsJobConfig);
}
