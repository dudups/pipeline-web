package com.ezone.devops.plugins.job.build.cmake.docker.service.impl;

import com.ezone.devops.plugins.job.build.cmake.docker.dao.CmakeDockerConfigDao;
import com.ezone.devops.plugins.job.build.cmake.docker.model.CmakeDockerConfig;
import com.ezone.devops.plugins.job.build.cmake.docker.service.CmakeDockerConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CmakeDockerConfigServiceImpl implements CmakeDockerConfigService {

    @Autowired
    private CmakeDockerConfigDao dockerArtifactConfigDao;

    @Override
    public CmakeDockerConfig getById(Long id) {
        return dockerArtifactConfigDao.get(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return dockerArtifactConfigDao.delete(id);
    }

    @Override
    public CmakeDockerConfig saveConfig(CmakeDockerConfig cmakeDockerConfig) {
        return dockerArtifactConfigDao.save(cmakeDockerConfig);
    }
}
