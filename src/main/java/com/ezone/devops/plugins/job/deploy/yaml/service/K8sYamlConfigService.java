package com.ezone.devops.plugins.job.deploy.yaml.service;

import com.ezone.devops.plugins.job.deploy.yaml.model.K8sYamlConfig;

public interface K8sYamlConfigService {

    K8sYamlConfig findById(Long id);

    void saveConfig(K8sYamlConfig k8sYamlConfig);

    boolean deleteConfig(Long id);

}
