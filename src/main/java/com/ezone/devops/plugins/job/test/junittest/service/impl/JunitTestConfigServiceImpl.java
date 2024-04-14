package com.ezone.devops.plugins.job.test.junittest.service.impl;

import com.ezone.devops.plugins.job.test.junittest.dao.JunitTestConfigDao;
import com.ezone.devops.plugins.job.test.junittest.model.JunitTestConfig;
import com.ezone.devops.plugins.job.test.junittest.service.JunitTestConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JunitTestConfigServiceImpl implements JunitTestConfigService {

    @Autowired
    private JunitTestConfigDao junitTestConfigDao;

    @Override
    public boolean deleteConfig(Long id) {
        return junitTestConfigDao.delete(id);
    }

    @Override
    public JunitTestConfig getById(Long id) {
        return junitTestConfigDao.get(id);
    }

    @Override
    public JunitTestConfig saveConfig(JunitTestConfig junitTestConfig) {
        return junitTestConfigDao.save(junitTestConfig);
    }
}