package com.ezone.devops.plugins.job.other.docker.service;

import com.ezone.devops.plugins.job.other.docker.model.DockerExecutorBuild;

public interface DockerExecutorBuildService {

    boolean updateDockerExecutorBuild(DockerExecutorBuild dockerExecutorBuild);

    DockerExecutorBuild getById(Long id);

    DockerExecutorBuild saveDockerExecutorBuild(DockerExecutorBuild dockerExecutorBuild);
}
