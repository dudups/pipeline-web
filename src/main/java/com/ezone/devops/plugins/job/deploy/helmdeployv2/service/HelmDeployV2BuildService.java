package com.ezone.devops.plugins.job.deploy.helmdeployv2.service;

import com.ezone.devops.plugins.job.deploy.helmdeployv2.model.HelmDeployV2Build;

public interface HelmDeployV2BuildService {

    HelmDeployV2Build getById(Long id);

    HelmDeployV2Build saveBuild(HelmDeployV2Build helmDeployV2Build);

    boolean updateBuild(HelmDeployV2Build helmDeployV2Build);

}
