package com.ezone.devops.plugins.job.build.gradle.docker.service.impl;

import com.ezone.devops.plugins.job.build.gradle.docker.dao.GradleDockerConfigDao;
import com.ezone.devops.plugins.job.build.gradle.docker.model.GradleDockerConfig;
import com.ezone.devops.plugins.job.build.gradle.docker.service.GradleDockerConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GradleDockerConfigServiceImpl implements GradleDockerConfigService {

    @Autowired
    private GradleDockerConfigDao gradleDockerConfigDao;

    @Override
    public GradleDockerConfig getById(Long id) {
        return gradleDockerConfigDao.get(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return gradleDockerConfigDao.delete(id);
    }

    @Override
    public GradleDockerConfig saveConfig(GradleDockerConfig gradleDockerConfig) {
        return gradleDockerConfigDao.save(gradleDockerConfig);
    }
}
