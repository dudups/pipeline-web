package com.ezone.devops.plugins.job.deploy.ezk8s.service;

import com.ezone.devops.plugins.job.deploy.ezk8s.model.Ezk8sDeployBuild;

public interface Ezk8sDeployBuildService {

    Ezk8sDeployBuild getById(Long id);

    void saveBuild(Ezk8sDeployBuild ezk8SDeployBuild);

    boolean updateBuild(Ezk8sDeployBuild ezk8SDeployBuild);

}
