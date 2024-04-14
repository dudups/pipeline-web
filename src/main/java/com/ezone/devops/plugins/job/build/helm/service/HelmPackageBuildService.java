package com.ezone.devops.plugins.job.build.helm.service;

import com.ezone.devops.plugins.job.build.helm.model.HelmPackageBuild;

public interface HelmPackageBuildService {

    boolean updateBuild(HelmPackageBuild helmPackageBuild);

    HelmPackageBuild getById(Long id);

    HelmPackageBuild saveBuild(HelmPackageBuild helmPackageBuild);
}
