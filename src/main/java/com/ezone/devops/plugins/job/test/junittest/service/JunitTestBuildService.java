package com.ezone.devops.plugins.job.test.junittest.service;

import com.ezone.devops.plugins.job.test.junittest.model.JunitTestBuild;

public interface JunitTestBuildService {

    JunitTestBuild saveBuild(JunitTestBuild junitTestBuild);

    JunitTestBuild getById(Long id);

    boolean updateBuild(JunitTestBuild junitTestBuild);
}
