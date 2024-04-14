package com.ezone.devops.plugins.job.deploy.helmdeploy.service.impl;

import com.ezone.devops.plugins.job.deploy.helmdeploy.dao.HelmDeployConfigDao;
import com.ezone.devops.plugins.job.deploy.helmdeploy.model.HelmDeployConfig;
import com.ezone.devops.plugins.job.deploy.helmdeploy.service.HelmDeployConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HelmDeployConfigServiceImpl implements HelmDeployConfigService {

    @Autowired
    private HelmDeployConfigDao helmDeployConfigDao;

    @Override
    public HelmDeployConfig findById(Long id) {
        return helmDeployConfigDao.get(id);
    }

    @Override
    public HelmDeployConfig saveConfig(HelmDeployConfig helmDeployConfig) {
        return helmDeployConfigDao.save(helmDeployConfig);
    }

    @Override
    public boolean updateConfig(HelmDeployConfig helmDeployConfig) {
        return helmDeployConfigDao.update(helmDeployConfig);
    }

    @Override
    public boolean deleteConfig(Long id) {
        return helmDeployConfigDao.delete(id);
    }

    @Override
    public List<HelmDeployConfig> getAll() {
        return helmDeployConfigDao.findAll();
    }
}
