package com.ezone.devops.plugins.job.build.gradle.file.service.impl;

import com.ezone.devops.plugins.job.build.gradle.file.dao.GradleConfigDao;
import com.ezone.devops.plugins.job.build.gradle.file.model.GradleConfig;
import com.ezone.devops.plugins.job.build.gradle.file.service.GradleConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GradleConfigServiceImpl implements GradleConfigService {

    @Autowired
    private GradleConfigDao gradleConfigDao;

    @Override
    public GradleConfig getById(Long id) {
        return gradleConfigDao.get(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return gradleConfigDao.delete(id);
    }

    @Override
    public GradleConfig saveConfig(GradleConfig gradleConfig) {
        return gradleConfigDao.save(gradleConfig);
    }
}
