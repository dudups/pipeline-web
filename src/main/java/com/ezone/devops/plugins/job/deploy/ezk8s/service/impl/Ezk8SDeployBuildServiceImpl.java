package com.ezone.devops.plugins.job.deploy.ezk8s.service.impl;

import com.ezone.devops.plugins.job.deploy.ezk8s.dao.Ezk8sDeployBuildDao;
import com.ezone.devops.plugins.job.deploy.ezk8s.model.Ezk8sDeployBuild;
import com.ezone.devops.plugins.job.deploy.ezk8s.service.Ezk8sDeployBuildService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Ezk8SDeployBuildServiceImpl implements Ezk8sDeployBuildService {

    @Autowired
    private Ezk8sDeployBuildDao ezk8SDeployBuildDao;

    @Override
    public Ezk8sDeployBuild getById(Long id) {
        return ezk8SDeployBuildDao.get(id);
    }

    @Override
    public void saveBuild(Ezk8sDeployBuild ezk8SDeployBuild) {
        ezk8SDeployBuildDao.save(ezk8SDeployBuild);
    }

    @Override
    public boolean updateBuild(Ezk8sDeployBuild ezk8SDeployBuild) {
        return ezk8SDeployBuildDao.update(ezk8SDeployBuild);
    }
}
