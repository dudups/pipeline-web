package com.ezone.devops.plugins.job.build.ant.file.service.impl;

import com.ezone.devops.plugins.job.build.ant.file.dao.AntBuildDao;
import com.ezone.devops.plugins.job.build.ant.file.model.AntBuild;
import com.ezone.devops.plugins.job.build.ant.file.service.AntBuildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AntBuildServiceImpl implements AntBuildService {

    @Autowired
    private AntBuildDao antBuildDao;

    @Override
    public AntBuild getById(Long id) {
        return antBuildDao.get(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return antBuildDao.delete(id);
    }

    @Override
    public AntBuild saveBuild(AntBuild antBuild) {
        return antBuildDao.save(antBuild);
    }

    @Override
    public boolean updateBuild(AntBuild antBuild) {
        return antBuildDao.update(antBuild);
    }
}
