package com.ezone.devops.plugins.job.test.jacocotest.service;

import com.ezone.devops.plugins.job.test.jacocotest.model.JacocoTestBuild;

public interface JacocoTestBuildService {

    JacocoTestBuild saveBuild(JacocoTestBuild mavenTestBuild);

    JacocoTestBuild getById(Long id);

    boolean updateBuild(JacocoTestBuild mavenTestBuild);
}
