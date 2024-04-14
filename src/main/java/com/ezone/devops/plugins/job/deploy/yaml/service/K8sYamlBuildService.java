package com.ezone.devops.plugins.job.deploy.yaml.service;

import com.ezone.devops.plugins.job.deploy.yaml.model.K8sYamlBuild;

public interface K8sYamlBuildService {

    K8sYamlBuild findById(Long id);

    K8sYamlBuild saveBuild(K8sYamlBuild k8sYamlBuild);

    boolean deleteBuild(Long id);

}
