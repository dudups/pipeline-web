package com.ezone.devops.plugins.job.build.maven.docker.service.impl;

import com.ezone.devops.plugins.job.build.maven.docker.dao.MavenDockerConfigDao;
import com.ezone.devops.plugins.job.build.maven.docker.model.MavenDockerConfig;
import com.ezone.devops.plugins.job.build.maven.docker.service.MavenDockerConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MavenDockerConfigServiceImpl implements MavenDockerConfigService {

    @Autowired
    private MavenDockerConfigDao dockerArtifactConfigDao;

    @Override
    public MavenDockerConfig getById(Long id) {
        return dockerArtifactConfigDao.get(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return dockerArtifactConfigDao.delete(id);
    }

    @Override
    public MavenDockerConfig saveConfig(MavenDockerConfig mavenDockerConfig) {
        return dockerArtifactConfigDao.save(mavenDockerConfig);
    }
}
