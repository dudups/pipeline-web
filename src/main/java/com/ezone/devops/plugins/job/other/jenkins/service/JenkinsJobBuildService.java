package com.ezone.devops.plugins.job.other.jenkins.service;

import com.ezone.devops.plugins.job.other.jenkins.model.JenkinsJobBuild;

public interface JenkinsJobBuildService {

    boolean updateJenkinsJobBuild(JenkinsJobBuild jenkinsJobBuild);

    JenkinsJobBuild getById(Long id);

    JenkinsJobBuild saveJenkinsJobBuild(JenkinsJobBuild jenkinsJobBuild);
}
