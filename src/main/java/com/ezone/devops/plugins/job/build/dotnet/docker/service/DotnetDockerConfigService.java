package com.ezone.devops.plugins.job.build.dotnet.docker.service;


import com.ezone.devops.plugins.job.build.dotnet.docker.model.DotnetDockerConfig;

public interface DotnetDockerConfigService {

    DotnetDockerConfig getById(Long id);

    boolean deleteById(Long id);

    DotnetDockerConfig saveConfig(DotnetDockerConfig dotnetDockerConfig);

}
