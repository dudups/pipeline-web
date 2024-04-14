package com.ezone.devops.plugins.job.other.docker.service.impl;

import com.ezone.devops.plugins.job.other.docker.dao.DockerExecutorBuildDao;
import com.ezone.devops.plugins.job.other.docker.model.DockerExecutorBuild;
import com.ezone.devops.plugins.job.other.docker.service.DockerExecutorBuildService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DockerExecutorBuildServiceImpl implements DockerExecutorBuildService {

    @Autowired
    private DockerExecutorBuildDao dockerExecutorBuildDao;

    @Override
    public boolean updateDockerExecutorBuild(DockerExecutorBuild dockerExecutorBuild) {
        return dockerExecutorBuildDao.update(dockerExecutorBuild);
    }

    @Override
    public DockerExecutorBuild getById(Long id) {
        return dockerExecutorBuildDao.get(id);
    }

    @Override
    public DockerExecutorBuild saveDockerExecutorBuild(DockerExecutorBuild dockerExecutorBuild) {
        return dockerExecutorBuildDao.save(dockerExecutorBuild);
    }
}
