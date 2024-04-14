package com.ezone.devops.plugins.job.build.maven.docker.service.impl;

import com.ezone.devops.plugins.job.build.maven.docker.dao.MavenDockerBuildDao;
import com.ezone.devops.plugins.job.build.maven.docker.model.MavenDockerBuild;
import com.ezone.devops.plugins.job.build.maven.docker.service.MavenDockerBuildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MavenDockerBuildServiceImpl implements MavenDockerBuildService {

    @Autowired
    private MavenDockerBuildDao dockerArtifactBuildDao;

    @Override
    public MavenDockerBuild getById(Long id) {
        return dockerArtifactBuildDao.get(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return dockerArtifactBuildDao.delete(id);
    }

    @Override
    public MavenDockerBuild saveBuild(MavenDockerBuild mavenDockerBuild) {
        return dockerArtifactBuildDao.save(mavenDockerBuild);
    }

    @Override
    public boolean updateBuild(MavenDockerBuild mavenDockerBuild) {
        return dockerArtifactBuildDao.update(mavenDockerBuild);
    }
}
