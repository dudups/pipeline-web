package com.ezone.devops.plugins.job.build.python.docker.service.impl;

import com.ezone.devops.plugins.job.build.python.docker.dao.PythonDockerConfigDao;
import com.ezone.devops.plugins.job.build.python.docker.model.PythonDockerConfig;
import com.ezone.devops.plugins.job.build.python.docker.service.PythonDockerConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PythonDockerConfigServiceImpl implements PythonDockerConfigService {

    @Autowired
    private PythonDockerConfigDao dockerArtifactConfigDao;

    @Override
    public PythonDockerConfig getById(Long id) {
        return dockerArtifactConfigDao.get(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return dockerArtifactConfigDao.delete(id);
    }

    @Override
    public PythonDockerConfig saveConfig(PythonDockerConfig pythonDockerConfig) {
        return dockerArtifactConfigDao.save(pythonDockerConfig);
    }
}
