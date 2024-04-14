package com.ezone.devops.plugins.job.build.dotnet.docker.service.impl;

import com.ezone.devops.plugins.job.build.dotnet.docker.dao.DotnetDockerConfigDao;
import com.ezone.devops.plugins.job.build.dotnet.docker.model.DotnetDockerConfig;
import com.ezone.devops.plugins.job.build.dotnet.docker.service.DotnetDockerConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DotnetDockerConfigServiceImpl implements DotnetDockerConfigService {

    @Autowired
    private DotnetDockerConfigDao dockerArtifactConfigDao;

    @Override
    public DotnetDockerConfig getById(Long id) {
        return dockerArtifactConfigDao.get(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return dockerArtifactConfigDao.delete(id);
    }

    @Override
    public DotnetDockerConfig saveConfig(DotnetDockerConfig dotnetDockerConfig) {
        return dockerArtifactConfigDao.save(dotnetDockerConfig);
    }
}
