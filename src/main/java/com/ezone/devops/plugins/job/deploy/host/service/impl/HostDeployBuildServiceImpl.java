package com.ezone.devops.plugins.job.deploy.host.service.impl;

import com.ezone.devops.plugins.job.deploy.host.dao.HostDeployBuildDao;
import com.ezone.devops.plugins.job.deploy.host.model.HostDeployBuild;
import com.ezone.devops.plugins.job.deploy.host.service.HostDeployBuildService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HostDeployBuildServiceImpl implements HostDeployBuildService {

    @Autowired
    private HostDeployBuildDao hostDeployBuildDao;

    @Override
    public HostDeployBuild getById(Long id) {
        return hostDeployBuildDao.get(id);
    }

    @Override
    public void saveBuild(HostDeployBuild hostDeployBuild) {
        hostDeployBuildDao.save(hostDeployBuild);
    }

    @Override
    public boolean updateBuild(HostDeployBuild hostDeployBuild) {
        return hostDeployBuildDao.update(hostDeployBuild);
    }
}
