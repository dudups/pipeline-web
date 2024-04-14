package com.ezone.devops.plugins.job.build.ant.docker.service;


import com.ezone.devops.plugins.job.build.ant.docker.model.AntDockerConfig;

public interface AntDockerConfigService {

    AntDockerConfig getById(Long id);

    boolean deleteById(Long id);

    AntDockerConfig saveConfig(AntDockerConfig antDockerConfig);

}
