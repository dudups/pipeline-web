package com.ezone.devops.plugins.job.deploy.helmdeployv2.service;

import com.ezone.devops.plugins.job.deploy.helmdeployv2.model.HelmDeployV2Config;

public interface HelmDeployV2ConfigService {

    HelmDeployV2Config findById(Long id);

    void saveConfig(HelmDeployV2Config helmDeployV2Config);

    boolean deleteConfig(Long id);

}
