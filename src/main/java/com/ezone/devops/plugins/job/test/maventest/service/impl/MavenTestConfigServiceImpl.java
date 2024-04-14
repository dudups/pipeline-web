package com.ezone.devops.plugins.job.test.maventest.service.impl;

import com.ezone.devops.plugins.job.test.maventest.dao.MavenTestConfigDao;
import com.ezone.devops.plugins.job.test.maventest.model.MavenTestConfig;
import com.ezone.devops.plugins.job.test.maventest.service.MavenTestConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MavenTestConfigServiceImpl implements MavenTestConfigService {

    @Autowired
    private MavenTestConfigDao mavenTestConfigDao;

    @Override
    public boolean deleteConfig(Long id) {
        return mavenTestConfigDao.delete(id);
    }

    @Override
    public MavenTestConfig getById(Long id) {
        return mavenTestConfigDao.get(id);
    }

    @Override
    public MavenTestConfig saveConfig(MavenTestConfig mavenTestConfig) {
        return mavenTestConfigDao.save(mavenTestConfig);
    }
}