package com.ezone.devops.plugins.job.build.npm.docker.service.impl;

import com.ezone.devops.plugins.job.build.npm.docker.dao.NpmDockerConfigDao;
import com.ezone.devops.plugins.job.build.npm.docker.model.NpmDockerConfig;
import com.ezone.devops.plugins.job.build.npm.docker.service.NpmDockerConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NpmDockerConfigServiceImpl implements NpmDockerConfigService {

    @Autowired
    private NpmDockerConfigDao dockerArtifactConfigDao;

    @Override
    public NpmDockerConfig getById(Long id) {
        return dockerArtifactConfigDao.get(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return dockerArtifactConfigDao.delete(id);
    }

    @Override
    public NpmDockerConfig saveConfig(NpmDockerConfig npmDockerConfig) {
        return dockerArtifactConfigDao.save(npmDockerConfig);
    }
}
