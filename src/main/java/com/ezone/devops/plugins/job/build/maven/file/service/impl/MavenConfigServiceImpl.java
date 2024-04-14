package com.ezone.devops.plugins.job.build.maven.file.service.impl;

import com.ezone.devops.plugins.job.build.maven.file.dao.MavenConfigDao;
import com.ezone.devops.plugins.job.build.maven.file.model.MavenConfig;
import com.ezone.devops.plugins.job.build.maven.file.service.MavenConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MavenConfigServiceImpl implements MavenConfigService {

    @Autowired
    private MavenConfigDao mavenConfigDao;

    @Override
    public MavenConfig getById(Long id) {
        return mavenConfigDao.get(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return mavenConfigDao.delete(id);
    }

    @Override
    public MavenConfig saveConfig(MavenConfig mavenConfig) {
        return mavenConfigDao.save(mavenConfig);
    }
}
