package com.ezone.devops.plugins.job.test.eztest.performance.service.impl;

import com.ezone.devops.plugins.job.test.eztest.performance.dao.EzTestPerformanceConfigDao;
import com.ezone.devops.plugins.job.test.eztest.performance.model.EzTestPerformanceConfig;
import com.ezone.devops.plugins.job.test.eztest.performance.service.EzTestPerformanceConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EzTestPerformanceConfigServiceImpl implements EzTestPerformanceConfigService {

    @Autowired
    private EzTestPerformanceConfigDao ezTestPerformanceConfigDao;

    @Override
    public boolean deleteConfig(Long id) {
        return ezTestPerformanceConfigDao.delete(id);
    }

    @Override
    public EzTestPerformanceConfig getById(Long id) {
        return ezTestPerformanceConfigDao.get(id);
    }

    @Override
    public EzTestPerformanceConfig saveConfig(EzTestPerformanceConfig ezTestPerformanceConfig) {
        return ezTestPerformanceConfigDao.save(ezTestPerformanceConfig);
    }
}