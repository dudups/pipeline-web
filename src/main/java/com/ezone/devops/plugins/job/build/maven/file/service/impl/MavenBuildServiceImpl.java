package com.ezone.devops.plugins.job.build.maven.file.service.impl;

import com.ezone.devops.plugins.job.build.maven.file.dao.MavenBuildDao;
import com.ezone.devops.plugins.job.build.maven.file.model.MavenBuild;
import com.ezone.devops.plugins.job.build.maven.file.service.MavenBuildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MavenBuildServiceImpl implements MavenBuildService {

    @Autowired
    private MavenBuildDao mavenBuildDao;

    @Override
    public MavenBuild getById(Long id) {
        return mavenBuildDao.get(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return mavenBuildDao.delete(id);
    }

    @Override
    public MavenBuild saveBuild(MavenBuild mavenBuild) {
        return mavenBuildDao.save(mavenBuild);
    }

    @Override
    public boolean updateBuild(MavenBuild mavenBuild) {
        return mavenBuildDao.update(mavenBuild);
    }
}
