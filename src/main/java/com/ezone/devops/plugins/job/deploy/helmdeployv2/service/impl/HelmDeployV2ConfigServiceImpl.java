package com.ezone.devops.plugins.job.deploy.helmdeployv2.service.impl;

import com.ezone.devops.plugins.job.deploy.helmdeployv2.dao.HelmDeployV2ConfigDao;
import com.ezone.devops.plugins.job.deploy.helmdeployv2.model.HelmDeployV2Config;
import com.ezone.devops.plugins.job.deploy.helmdeployv2.service.HelmDeployV2ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HelmDeployV2ConfigServiceImpl implements HelmDeployV2ConfigService {

    @Autowired
    private HelmDeployV2ConfigDao helmDeployV2ConfigDao;

    @Override
    public HelmDeployV2Config findById(Long id) {
        return helmDeployV2ConfigDao.get(id);
    }

    @Override
    public void saveConfig(HelmDeployV2Config helmDeployV2Config) {
        helmDeployV2ConfigDao.save(helmDeployV2Config);
    }

    @Override
    public boolean deleteConfig(Long id) {
        return helmDeployV2ConfigDao.delete(id);
    }
}
