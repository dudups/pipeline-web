package com.ezone.devops.plugins.job.build.cmake.docker.service;


import com.ezone.devops.plugins.job.build.cmake.docker.model.CmakeDockerConfig;

public interface CmakeDockerConfigService {

    CmakeDockerConfig getById(Long id);

    boolean deleteById(Long id);

    CmakeDockerConfig saveConfig(CmakeDockerConfig cmakeDockerConfig);

}
