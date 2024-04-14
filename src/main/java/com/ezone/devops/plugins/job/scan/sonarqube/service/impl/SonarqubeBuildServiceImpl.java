package com.ezone.devops.plugins.job.scan.sonarqube.service.impl;

import com.ezone.devops.plugins.job.scan.sonarqube.dao.SonarqubeBuildDao;
import com.ezone.devops.plugins.job.scan.sonarqube.model.SonarqubeBuild;
import com.ezone.devops.plugins.job.scan.sonarqube.service.SonarqubeBuildService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SonarqubeBuildServiceImpl implements SonarqubeBuildService {

    @Autowired
    private SonarqubeBuildDao sonarqubeBuildDao;

    @Override
    public boolean updateBuild(SonarqubeBuild sonarqubeBuild) {
        return sonarqubeBuildDao.update(sonarqubeBuild);
    }

    @Override
    public SonarqubeBuild getById(Long id) {
        return sonarqubeBuildDao.get(id);
    }

    @Override
    public SonarqubeBuild saveBuild(SonarqubeBuild sonarqubeBuild) {
        return sonarqubeBuildDao.save(sonarqubeBuild);
    }
}
