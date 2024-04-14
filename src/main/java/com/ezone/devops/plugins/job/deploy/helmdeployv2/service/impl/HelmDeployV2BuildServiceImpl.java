package com.ezone.devops.plugins.job.deploy.helmdeployv2.service.impl;

import com.ezone.devops.plugins.job.deploy.helmdeployv2.dao.HelmDeployV2BuildDao;
import com.ezone.devops.plugins.job.deploy.helmdeployv2.model.HelmDeployV2Build;
import com.ezone.devops.plugins.job.deploy.helmdeployv2.service.HelmDeployV2BuildService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HelmDeployV2BuildServiceImpl implements HelmDeployV2BuildService {

    @Autowired
    private HelmDeployV2BuildDao helmDeployV2BuildDao;

    @Override
    public HelmDeployV2Build getById(Long id) {
        return helmDeployV2BuildDao.get(id);
    }

    @Override
    public HelmDeployV2Build saveBuild(HelmDeployV2Build helmDeployV2Build) {
        return helmDeployV2BuildDao.save(helmDeployV2Build);
    }

    @Override
    public boolean updateBuild(HelmDeployV2Build helmDeployV2Build) {
        return helmDeployV2BuildDao.update(helmDeployV2Build);
    }
}
