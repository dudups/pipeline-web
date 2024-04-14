package com.ezone.devops.plugins.job.test.junittest.service;

import com.ezone.devops.plugins.job.test.junittest.model.JunitTestConfig;

public interface JunitTestConfigService {

    boolean deleteConfig(Long id);

    JunitTestConfig getById(Long id);

    JunitTestConfig saveConfig(JunitTestConfig junitTestConfig);
}
