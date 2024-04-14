package com.ezone.devops.plugins.job.deploy.host.service.impl;

import com.ezone.devops.plugins.job.deploy.host.dao.HostDeployConfigDao;
import com.ezone.devops.plugins.job.deploy.host.model.HostDeployConfig;
import com.ezone.devops.plugins.job.deploy.host.service.HostDeployConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HostDeployConfigServiceImpl implements HostDeployConfigService {

    @Autowired
    private HostDeployConfigDao hostDeployConfigDao;

    @Override
    public HostDeployConfig getById(Long id) {
        return hostDeployConfigDao.get(id);
    }

    @Override
    public void saveConfig(HostDeployConfig hostDeployConfig) {
        hostDeployConfigDao.save(hostDeployConfig);
    }

    @Override
    public boolean deleteConfig(Long id) {
        return hostDeployConfigDao.delete(id);
    }
}
