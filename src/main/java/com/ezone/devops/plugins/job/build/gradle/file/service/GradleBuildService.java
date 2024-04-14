package com.ezone.devops.plugins.job.build.gradle.file.service;


import com.ezone.devops.plugins.job.build.gradle.file.model.GradleBuild;

public interface GradleBuildService {

    GradleBuild getById(Long id);

    boolean deleteById(Long id);

    GradleBuild saveBuild(GradleBuild gradleBuild);

    boolean updateBuild(GradleBuild gradleBuild);
}
