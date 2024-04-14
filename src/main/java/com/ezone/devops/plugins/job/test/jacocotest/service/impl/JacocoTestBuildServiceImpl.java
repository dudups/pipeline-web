package com.ezone.devops.plugins.job.test.jacocotest.service.impl;

import com.ezone.devops.plugins.job.test.jacocotest.dao.JacocoTestBuildDao;
import com.ezone.devops.plugins.job.test.jacocotest.model.JacocoTestBuild;
import com.ezone.devops.plugins.job.test.jacocotest.service.JacocoTestBuildService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JacocoTestBuildServiceImpl implements JacocoTestBuildService {

    @Autowired
    private JacocoTestBuildDao jacocoTestBuildDao;

    @Override
    public boolean updateBuild(JacocoTestBuild mavenTestBuild) {
        return jacocoTestBuildDao.update(mavenTestBuild);
    }

    @Override
    public JacocoTestBuild getById(Long id) {
        return jacocoTestBuildDao.get(id);
    }

    @Override
    public JacocoTestBuild saveBuild(JacocoTestBuild mavenTestBuild) {
        return jacocoTestBuildDao.save(mavenTestBuild);
    }
}
