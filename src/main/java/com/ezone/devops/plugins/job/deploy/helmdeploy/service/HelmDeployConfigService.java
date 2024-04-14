package com.ezone.devops.plugins.job.deploy.helmdeploy.service;

import com.ezone.devops.plugins.job.deploy.helmdeploy.model.HelmDeployConfig;

import java.util.List;

public interface HelmDeployConfigService {

    HelmDeployConfig findById(Long id);

    HelmDeployConfig saveConfig(HelmDeployConfig helmDeployConfig);

    boolean updateConfig(HelmDeployConfig helmDeployConfig);

    boolean deleteConfig(Long id);

    List<HelmDeployConfig> getAll();
}
