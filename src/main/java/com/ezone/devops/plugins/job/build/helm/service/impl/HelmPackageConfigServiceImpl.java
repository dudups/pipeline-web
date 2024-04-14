package com.ezone.devops.plugins.job.build.helm.service.impl;

import com.ezone.devops.plugins.job.build.helm.dao.HelmPackageConfigDao;
import com.ezone.devops.plugins.job.build.helm.model.HelmPackageConfig;
import com.ezone.devops.plugins.job.build.helm.service.HelmPackageConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HelmPackageConfigServiceImpl implements HelmPackageConfigService {

    @Autowired
    private HelmPackageConfigDao helmPackageConfigDao;

    @Override
    public boolean deleteConfig(Long id) {
        return helmPackageConfigDao.delete(id);
    }

    @Override
    public HelmPackageConfig getById(Long id) {
        return helmPackageConfigDao.get(id);
    }

    @Override
    public HelmPackageConfig saveConfig(HelmPackageConfig helmPackageConfig) {
        return helmPackageConfigDao.save(helmPackageConfig);
    }
}