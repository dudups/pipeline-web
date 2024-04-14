package com.ezone.devops.plugins.job.build.helm.service.impl;

import com.ezone.devops.plugins.job.build.helm.dao.HelmPackageBuildDao;
import com.ezone.devops.plugins.job.build.helm.model.HelmPackageBuild;
import com.ezone.devops.plugins.job.build.helm.service.HelmPackageBuildService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HelmPackageBuildServiceImpl implements HelmPackageBuildService {

    @Autowired
    private HelmPackageBuildDao helmPackageBuildDao;

    @Override
    public boolean updateBuild(HelmPackageBuild helmPackageBuild) {
        return helmPackageBuildDao.update(helmPackageBuild);
    }

    @Override
    public HelmPackageBuild getById(Long id) {
        return helmPackageBuildDao.get(id);
    }

    @Override
    public HelmPackageBuild saveBuild(HelmPackageBuild helmPackageBuild) {
        return helmPackageBuildDao.save(helmPackageBuild);
    }
}
