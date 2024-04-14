package com.ezone.devops.plugins.job.deploy.helmdeploy.service.impl;

import com.ezone.devops.plugins.job.deploy.helmdeploy.dao.HelmDeployBuildDao;
import com.ezone.devops.plugins.job.deploy.helmdeploy.model.HelmDeployBuild;
import com.ezone.devops.plugins.job.deploy.helmdeploy.service.HelmDeployBuildService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HelmDeployBuildServiceImpl implements HelmDeployBuildService {

    @Autowired
    private HelmDeployBuildDao helmDeployBuildDao;

    @Override
    public HelmDeployBuild getById(Long id) {
        return helmDeployBuildDao.get(id);
    }

    @Override
    public HelmDeployBuild saveBuild(HelmDeployBuild helmDeployBuild) {
        return helmDeployBuildDao.save(helmDeployBuild);
    }

    @Override
    public boolean updateBuild(HelmDeployBuild helmDeployBuild) {
        return helmDeployBuildDao.update(helmDeployBuild);
    }
}
