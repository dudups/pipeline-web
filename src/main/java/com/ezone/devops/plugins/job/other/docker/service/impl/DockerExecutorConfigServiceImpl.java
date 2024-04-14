package com.ezone.devops.plugins.job.release.service.impl;

import com.ezone.devops.plugins.job.other.docker.dao.DockerExecutorConfigDao;
import com.ezone.devops.plugins.job.other.docker.model.DockerExecutorConfig;
import com.ezone.devops.plugins.job.other.docker.service.DockerExecutorConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DockerExecutorConfigServiceImpl implements DockerExecutorConfigService {

    @Autowired
    private DockerExecutorConfigDao dockerExecutorConfigDao;

    @Override
    public boolean deleteDockerExecutorConfig(Long id) {
        return dockerExecutorConfigDao.delete(id);
    }

    @Override
    public DockerExecutorConfig getById(Long id) {
        return dockerExecutorConfigDao.get(id);
    }

    @Override
    public DockerExecutorConfig saveDockerExecutorConfig(DockerExecutorConfig dockerExecutorConfig) {
        return dockerExecutorConfigDao.save(dockerExecutorConfig);
    }
}