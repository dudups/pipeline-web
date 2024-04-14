package com.ezone.devops.plugins.job.test.maventest.service;

import com.ezone.devops.plugins.job.test.maventest.model.MavenTestBuild;

public interface MavenTestBuildService {

    MavenTestBuild saveBuild(MavenTestBuild mavenTestBuild);

    MavenTestBuild getById(Long id);

    boolean updateBuild(MavenTestBuild mavenTestBuild);
}
