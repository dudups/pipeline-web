package com.ezone.devops.plugins.job.deploy.host.service;

import com.ezone.devops.plugins.job.deploy.host.model.HostDeployBuild;

public interface HostDeployBuildService {

    HostDeployBuild getById(Long id);

    void saveBuild(HostDeployBuild hostDeployBuild);

    boolean updateBuild(HostDeployBuild hostDeployBuild);

}
