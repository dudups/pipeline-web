package com.ezone.devops.plugins.job.scan.sonarqube.service;

import com.ezone.devops.plugins.job.scan.sonarqube.model.SonarqubeConfig;

import java.util.List;

public interface SonarqubeConfigService {

    List<SonarqubeConfig> getAll();

    boolean deleteConfig(Long id);

    SonarqubeConfig getById(Long id);

    SonarqubeConfig saveConfig(SonarqubeConfig sonarqubeConfig);

    boolean updateConfig(SonarqubeConfig sonarqubeConfig);
}
