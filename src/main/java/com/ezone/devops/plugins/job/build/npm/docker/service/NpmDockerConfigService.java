package com.ezone.devops.plugins.job.build.npm.docker.service;


import com.ezone.devops.plugins.job.build.npm.docker.model.NpmDockerConfig;

public interface NpmDockerConfigService {

    NpmDockerConfig getById(Long id);

    boolean deleteById(Long id);

    NpmDockerConfig saveConfig(NpmDockerConfig npmDockerConfig);

}
