package com.ezone.devops.plugins.job.build.gradle.docker.service;


import com.ezone.devops.plugins.job.build.gradle.docker.model.GradleDockerConfig;

public interface GradleDockerConfigService {

    GradleDockerConfig getById(Long id);

    boolean deleteById(Long id);

    GradleDockerConfig saveConfig(GradleDockerConfig gradleDockerConfig);

}
