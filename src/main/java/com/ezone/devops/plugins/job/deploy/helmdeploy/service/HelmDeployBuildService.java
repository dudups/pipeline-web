package com.ezone.devops.plugins.job.deploy.helmdeploy.service;


import com.ezone.devops.plugins.job.deploy.helmdeploy.model.HelmDeployBuild;

public interface HelmDeployBuildService {

    HelmDeployBuild getById(Long id);

    HelmDeployBuild saveBuild(HelmDeployBuild helmDeployBuild);

    boolean updateBuild(HelmDeployBuild helmDeployBuild);

}
