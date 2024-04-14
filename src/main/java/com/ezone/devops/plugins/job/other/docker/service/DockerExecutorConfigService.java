package com.ezone.devops.plugins.job.other.docker.service;

import com.ezone.devops.plugins.job.other.docker.model.DockerExecutorConfig;

public interface DockerExecutorConfigService {

    boolean deleteDockerExecutorConfig(Long id);

    DockerExecutorConfig getById(Long id);

    DockerExecutorConfig saveDockerExecutorConfig(DockerExecutorConfig dockerExecutorConfig);
}
