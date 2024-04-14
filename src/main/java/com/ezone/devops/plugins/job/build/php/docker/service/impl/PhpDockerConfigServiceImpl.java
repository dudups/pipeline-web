package com.ezone.devops.plugins.job.build.php.docker.service.impl;

import com.ezone.devops.plugins.job.build.php.docker.dao.PhpDockerConfigDao;
import com.ezone.devops.plugins.job.build.php.docker.model.PhpDockerConfig;
import com.ezone.devops.plugins.job.build.php.docker.service.PhpDockerConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhpDockerConfigServiceImpl implements PhpDockerConfigService {

    @Autowired
    private PhpDockerConfigDao dockerArtifactConfigDao;

    @Override
    public PhpDockerConfig getById(Long id) {
        return dockerArtifactConfigDao.get(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return dockerArtifactConfigDao.delete(id);
    }

    @Override
    public PhpDockerConfig saveConfig(PhpDockerConfig phpDockerConfig) {
        return dockerArtifactConfigDao.save(phpDockerConfig);
    }
}
