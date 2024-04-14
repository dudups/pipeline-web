package com.ezone.devops.plugins.job.build.gradle.docker.service;


import com.ezone.devops.plugins.job.build.gradle.docker.model.GradleDockerBuild;

public interface GradleDockerBuildService {

    GradleDockerBuild getById(Long id);

    boolean deleteById(Long id);

    GradleDockerBuild saveBuild(GradleDockerBuild gradleDockerBuild);

    boolean updateBuild(GradleDockerBuild gradleDockerBuild);
}
