package com.ezone.devops.plugins.job.test.eztest.api.service.impl;

import com.ezone.devops.plugins.job.test.eztest.api.dao.EzTestConfigDao;
import com.ezone.devops.plugins.job.test.eztest.api.model.EzTestConfig;
import com.ezone.devops.plugins.job.test.eztest.api.service.EzTestConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EzTestConfigServiceImpl implements EzTestConfigService {

    @Autowired
    private EzTestConfigDao ezTestConfigDao;

    @Override
    public boolean deleteConfig(Long id) {
        return ezTestConfigDao.delete(id);
    }

    @Override
    public EzTestConfig getById(Long id) {
        return ezTestConfigDao.get(id);
    }

    @Override
    public EzTestConfig saveConfig(EzTestConfig ezTestConfig) {
        return ezTestConfigDao.save(ezTestConfig);
    }
}