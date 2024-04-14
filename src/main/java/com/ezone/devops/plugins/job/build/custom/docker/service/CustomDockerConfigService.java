package com.ezone.devops.plugins.job.build.custom.docker.service;


import com.ezone.devops.plugins.job.build.custom.docker.model.CustomDockerConfig;

public interface CustomDockerConfigService {

    CustomDockerConfig getById(Long id);

    boolean deleteById(Long id);

    CustomDockerConfig saveConfig(CustomDockerConfig customDockerConfig);

}
