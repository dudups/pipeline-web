package com.ezone.devops.plugins.job.build.ant.docker.service.impl;

import com.ezone.devops.plugins.job.build.ant.docker.dao.AntDockerBuildDao;
import com.ezone.devops.plugins.job.build.ant.docker.model.AntDockerBuild;
import com.ezone.devops.plugins.job.build.ant.docker.service.AntDockerBuildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AntDockerBuildServiceImpl implements AntDockerBuildService {

    @Autowired
    private AntDockerBuildDao antDockerBuildDao;

    @Override
    public AntDockerBuild getById(Long id) {
        return antDockerBuildDao.get(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return antDockerBuildDao.delete(id);
    }

    @Override
    public AntDockerBuild saveBuild(AntDockerBuild antDockerBuild) {
        return antDockerBuildDao.save(antDockerBuild);
    }

    @Override
    public boolean updateBuild(AntDockerBuild antDockerBuild) {
        return antDockerBuildDao.update(antDockerBuild);
    }
}
