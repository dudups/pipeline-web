package com.ezone.devops.plugins.job.scan.sonarqube.service;

import com.ezone.devops.plugins.job.scan.sonarqube.model.SonarqubeBuild;

public interface SonarqubeBuildService {

    SonarqubeBuild saveBuild(SonarqubeBuild sonarqubeBuild);

    SonarqubeBuild getById(Long id);

    boolean updateBuild(SonarqubeBuild sonarqubeBuild);
}
