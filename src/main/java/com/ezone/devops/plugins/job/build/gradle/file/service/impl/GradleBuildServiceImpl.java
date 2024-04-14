package com.ezone.devops.plugins.job.build.gradle.file.service.impl;

import com.ezone.devops.plugins.job.build.gradle.file.dao.GradleBuildDao;
import com.ezone.devops.plugins.job.build.gradle.file.model.GradleBuild;
import com.ezone.devops.plugins.job.build.gradle.file.service.GradleBuildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GradleBuildServiceImpl implements GradleBuildService {

    @Autowired
    private GradleBuildDao gradleBuildDao;

    @Override
    public GradleBuild getById(Long id) {
        return gradleBuildDao.get(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return gradleBuildDao.delete(id);
    }

    @Override
    public GradleBuild saveBuild(GradleBuild gradleBuild) {
        return gradleBuildDao.save(gradleBuild);
    }

    @Override
    public boolean updateBuild(GradleBuild gradleBuild) {
        return gradleBuildDao.update(gradleBuild);
    }
}
