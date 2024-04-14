package com.ezone.devops.plugins.job.deploy.host.service;

import com.ezone.devops.plugins.job.deploy.host.model.HostDeployConfig;

public interface HostDeployConfigService {

    HostDeployConfig getById(Long id);

    void saveConfig(HostDeployConfig compileConfig);

    boolean deleteConfig(Long id);

}
