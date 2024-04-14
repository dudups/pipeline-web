package com.ezone.devops.plugins.job.build.gradle.docker.service.impl;

import com.ezone.devops.plugins.job.build.gradle.docker.dao.GradleDockerBuildDao;
import com.ezone.devops.plugins.job.build.gradle.docker.model.GradleDockerBuild;
import com.ezone.devops.plugins.job.build.gradle.docker.service.GradleDockerBuildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GradleDockerBuildServiceImpl implements GradleDockerBuildService {

    @Autowired
    private GradleDockerBuildDao gradleDockerBuildDao;

    @Override
    public GradleDockerBuild getById(Long id) {
        return gradleDockerBuildDao.get(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return gradleDockerBuildDao.delete(id);
    }

    @Override
    public GradleDockerBuild saveBuild(GradleDockerBuild gradleDockerBuild) {
        return gradleDockerBuildDao.save(gradleDockerBuild);
    }

    @Override
    public boolean updateBuild(GradleDockerBuild gradleDockerBuild) {
        return gradleDockerBuildDao.update(gradleDockerBuild);
    }
}
