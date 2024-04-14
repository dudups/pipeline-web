package com.ezone.devops.plugins.job.deploy.yaml.service.impl;

import com.ezone.devops.plugins.job.deploy.yaml.dao.K8sYamlBuildDao;
import com.ezone.devops.plugins.job.deploy.yaml.model.K8sYamlBuild;
import com.ezone.devops.plugins.job.deploy.yaml.service.K8sYamlBuildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class K8sYamlBuildServiceImpl implements K8sYamlBuildService {

    @Autowired
    private K8sYamlBuildDao k8sYamlBuildDao;

    @Override
    public K8sYamlBuild findById(Long id) {
        return k8sYamlBuildDao.get(id);
    }

    @Override
    public K8sYamlBuild saveBuild(K8sYamlBuild k8sYamlBuild) {
        return k8sYamlBuildDao.save(k8sYamlBuild);
    }

    @Override
    public boolean deleteBuild(Long id) {
        return k8sYamlBuildDao.delete(id);
    }
}
