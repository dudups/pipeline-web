package com.ezone.devops.plugins.job.build.go.docker.service.impl;

import com.ezone.devops.plugins.job.build.go.docker.dao.GoDockerConfigDao;
import com.ezone.devops.plugins.job.build.go.docker.model.GoDockerConfig;
import com.ezone.devops.plugins.job.build.go.docker.service.GoDockerConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoDockerConfigServiceImpl implements GoDockerConfigService {

    @Autowired
    private GoDockerConfigDao dockerArtifactConfigDao;

    @Override
    public GoDockerConfig getById(Long id) {
        return dockerArtifactConfigDao.get(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return dockerArtifactConfigDao.delete(id);
    }

    @Override
    public GoDockerConfig saveConfig(GoDockerConfig goDockerConfig) {
        return dockerArtifactConfigDao.save(goDockerConfig);
    }
}
