package com.ezone.devops.plugins.job.build.maven.docker.service;


import com.ezone.devops.plugins.job.build.maven.docker.model.MavenDockerConfig;

public interface MavenDockerConfigService {

    MavenDockerConfig getById(Long id);

    boolean deleteById(Long id);

    MavenDockerConfig saveConfig(MavenDockerConfig mavenDockerConfig);

}
