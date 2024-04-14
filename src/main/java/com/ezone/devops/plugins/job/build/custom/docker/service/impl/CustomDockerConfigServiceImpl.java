package com.ezone.devops.plugins.job.build.custom.docker.service.impl;

import com.ezone.devops.plugins.job.build.custom.docker.dao.CustomDockerConfigDao;
import com.ezone.devops.plugins.job.build.custom.docker.model.CustomDockerConfig;
import com.ezone.devops.plugins.job.build.custom.docker.service.CustomDockerConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomDockerConfigServiceImpl implements CustomDockerConfigService {

    @Autowired
    private CustomDockerConfigDao dockerArtifactConfigDao;

    @Override
    public CustomDockerConfig getById(Long id) {
        return dockerArtifactConfigDao.get(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return dockerArtifactConfigDao.delete(id);
    }

    @Override
    public CustomDockerConfig saveConfig(CustomDockerConfig customDockerConfig) {
        return dockerArtifactConfigDao.save(customDockerConfig);
    }
}
