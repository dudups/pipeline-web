package com.ezone.devops.plugins.job.scan.sonarqube.service.impl;

import com.ezone.devops.plugins.job.scan.sonarqube.dao.SonarqubeConfigDao;
import com.ezone.devops.plugins.job.scan.sonarqube.model.SonarqubeConfig;
import com.ezone.devops.plugins.job.scan.sonarqube.service.SonarqubeConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SonarqubeConfigServiceImpl implements SonarqubeConfigService {

    @Autowired
    private SonarqubeConfigDao sonarqubeConfigDao;

    @Override
    public List<SonarqubeConfig> getAll() {
        return sonarqubeConfigDao.findAll();
    }

    @Override
    public boolean deleteConfig(Long id) {
        return sonarqubeConfigDao.delete(id);
    }

    @Override
    public SonarqubeConfig getById(Long id) {
        return sonarqubeConfigDao.get(id);
    }

    @Override
    public SonarqubeConfig saveConfig(SonarqubeConfig sonarqubeConfig) {
        return sonarqubeConfigDao.save(sonarqubeConfig);
    }

    @Override
    public boolean updateConfig(SonarqubeConfig sonarqubeConfig) {
        return sonarqubeConfigDao.update(sonarqubeConfig);
    }
}