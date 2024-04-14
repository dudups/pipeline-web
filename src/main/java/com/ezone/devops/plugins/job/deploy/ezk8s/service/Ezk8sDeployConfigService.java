package com.ezone.devops.plugins.job.deploy.ezk8s.service;

import com.ezone.devops.plugins.job.deploy.ezk8s.model.Ezk8sDeployConfig;

public interface Ezk8sDeployConfigService {

    Ezk8sDeployConfig findById(Long id);

    void saveConfig(Ezk8sDeployConfig ezk8SDeployConfig);

    boolean deleteConfig(Long id);

}
