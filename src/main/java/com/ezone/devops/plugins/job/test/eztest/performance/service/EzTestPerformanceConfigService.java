package com.ezone.devops.plugins.job.test.eztest.performance.service;

import com.ezone.devops.plugins.job.test.eztest.performance.model.EzTestPerformanceConfig;

public interface EzTestPerformanceConfigService {

    boolean deleteConfig(Long id);

    EzTestPerformanceConfig getById(Long id);

    EzTestPerformanceConfig saveConfig(EzTestPerformanceConfig ezTestPerformanceConfig);
}
