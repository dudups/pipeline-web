package com.ezone.devops.plugins.job.test.maventest.service.impl;

import com.ezone.devops.plugins.job.test.maventest.dao.MavenTestBuildDao;
import com.ezone.devops.plugins.job.test.maventest.model.MavenTestBuild;
import com.ezone.devops.plugins.job.test.maventest.service.MavenTestBuildService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MavenTestBuildServiceImpl implements MavenTestBuildService {

    @Autowired
    private MavenTestBuildDao mavenTestBuildDao;

    @Override
    public boolean updateBuild(MavenTestBuild mavenTestBuild) {
        return mavenTestBuildDao.update(mavenTestBuild);
    }

    @Override
    public MavenTestBuild getById(Long id) {
        return mavenTestBuildDao.get(id);
    }

    @Override
    public MavenTestBuild saveBuild(MavenTestBuild mavenTestBuild) {
        return mavenTestBuildDao.save(mavenTestBuild);
    }
}
