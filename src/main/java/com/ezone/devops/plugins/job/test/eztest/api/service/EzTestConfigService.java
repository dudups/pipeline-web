package com.ezone.devops.plugins.job.test.eztest.api.service;

import com.ezone.devops.plugins.job.test.eztest.api.model.EzTestConfig;

public interface EzTestConfigService {

    boolean deleteConfig(Long id);

    EzTestConfig getById(Long id);

    EzTestConfig saveConfig(EzTestConfig ezTestConfig);
}
