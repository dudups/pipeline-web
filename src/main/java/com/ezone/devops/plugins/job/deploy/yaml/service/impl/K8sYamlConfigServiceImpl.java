package com.ezone.devops.plugins.job.deploy.yaml.service.impl;

import com.ezone.devops.plugins.job.deploy.yaml.dao.K8sYamlConfigDao;
import com.ezone.devops.plugins.job.deploy.yaml.model.K8sYamlConfig;
import com.ezone.devops.plugins.job.deploy.yaml.service.K8sYamlConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class K8sYamlConfigServiceImpl implements K8sYamlConfigService {

    @Autowired
    private K8sYamlConfigDao k8sYamlConfigDao;

    @Override
    public K8sYamlConfig findById(Long id) {
        return k8sYamlConfigDao.get(id);
    }

    @Override
    public void saveConfig(K8sYamlConfig k8sYamlConfig) {
        k8sYamlConfigDao.save(k8sYamlConfig);
    }

    @Override
    public boolean deleteConfig(Long id) {
        return k8sYamlConfigDao.delete(id);
    }
}
