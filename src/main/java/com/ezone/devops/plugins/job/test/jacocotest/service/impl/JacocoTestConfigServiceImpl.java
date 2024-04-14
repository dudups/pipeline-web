package com.ezone.devops.plugins.job.test.jacocotest.service.impl;

import com.ezone.devops.plugins.job.test.jacocotest.dao.JacocoTestConfigDao;
import com.ezone.devops.plugins.job.test.jacocotest.model.JacocoTestConfig;
import com.ezone.devops.plugins.job.test.jacocotest.service.JacocoTestConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JacocoTestConfigServiceImpl implements JacocoTestConfigService {

    @Autowired
    private JacocoTestConfigDao jacocoTestConfigDao;

    @Override
    public boolean deleteConfig(Long id) {
        return jacocoTestConfigDao.delete(id);
    }

    @Override
    public JacocoTestConfig getById(Long id) {
        return jacocoTestConfigDao.get(id);
    }

    @Override
    public JacocoTestConfig saveConfig(JacocoTestConfig jacocoTestConfig) {
        return jacocoTestConfigDao.save(jacocoTestConfig);
    }
}