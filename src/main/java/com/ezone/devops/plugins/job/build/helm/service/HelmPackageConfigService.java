package com.ezone.devops.plugins.job.build.helm.service;

import com.ezone.devops.plugins.job.build.helm.model.HelmPackageConfig;

public interface HelmPackageConfigService {

    boolean deleteConfig(Long id);

    HelmPackageConfig getById(Long id);

    HelmPackageConfig saveConfig(HelmPackageConfig helmPackageConfig);
}
