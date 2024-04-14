package com.ezone.devops.plugins.job.build.gradle.file.service;


import com.ezone.devops.plugins.job.build.gradle.file.model.GradleConfig;

public interface GradleConfigService {

    GradleConfig getById(Long id);

    boolean deleteById(Long id);

    GradleConfig saveConfig(GradleConfig gradleConfig);

}
