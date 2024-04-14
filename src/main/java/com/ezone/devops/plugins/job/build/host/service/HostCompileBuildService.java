package com.ezone.devops.plugins.job.build.host.service;

import com.ezone.devops.plugins.job.build.host.model.HostCompileBuild;

public interface HostCompileBuildService {

    HostCompileBuild getById(Long id);

    void saveBuild(HostCompileBuild hostCompileBuild);

    boolean updateBuild(HostCompileBuild hostCompileBuild);

}
