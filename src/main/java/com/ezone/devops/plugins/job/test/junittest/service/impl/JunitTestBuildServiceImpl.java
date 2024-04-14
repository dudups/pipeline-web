package com.ezone.devops.plugins.job.test.junittest.service.impl;

import com.ezone.devops.plugins.job.test.junittest.dao.JunitTestBuildDao;
import com.ezone.devops.plugins.job.test.junittest.model.JunitTestBuild;
import com.ezone.devops.plugins.job.test.junittest.service.JunitTestBuildService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JunitTestBuildServiceImpl implements JunitTestBuildService {

    @Autowired
    private JunitTestBuildDao junitTestBuildDao;

    @Override
    public boolean updateBuild(JunitTestBuild junitTestBuild) {
        return junitTestBuildDao.update(junitTestBuild);
    }

    @Override
    public JunitTestBuild getById(Long id) {
        return junitTestBuildDao.get(id);
    }

    @Override
    public JunitTestBuild saveBuild(JunitTestBuild junitTestBuild) {
        return junitTestBuildDao.save(junitTestBuild);
    }
}
