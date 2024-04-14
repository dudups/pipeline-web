package com.ezone.devops.plugins.job.build.go.docker.service;


import com.ezone.devops.plugins.job.build.go.docker.model.GoDockerConfig;

public interface GoDockerConfigService {

    GoDockerConfig getById(Long id);

    boolean deleteById(Long id);

    GoDockerConfig saveConfig(GoDockerConfig goDockerConfig);

}
