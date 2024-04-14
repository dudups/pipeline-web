package com.ezone.devops.plugins.job.build.ant.docker.service.impl;

import com.ezone.devops.plugins.job.build.ant.docker.dao.AntDockerConfigDao;
import com.ezone.devops.plugins.job.build.ant.docker.model.AntDockerConfig;
import com.ezone.devops.plugins.job.build.ant.docker.service.AntDockerConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AntDockerConfigServiceImpl implements AntDockerConfigService {

    @Autowired
    private AntDockerConfigDao antDockerConfigDao;

    @Override
    public AntDockerConfig getById(Long id) {
        return antDockerConfigDao.get(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return antDockerConfigDao.delete(id);
    }

    @Override
    public AntDockerConfig saveConfig(AntDockerConfig antDockerConfig) {
        return antDockerConfigDao.save(antDockerConfig);
    }
}
