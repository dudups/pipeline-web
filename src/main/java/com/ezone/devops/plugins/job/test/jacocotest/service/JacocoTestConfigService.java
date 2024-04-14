package com.ezone.devops.plugins.job.test.jacocotest.service;

import com.ezone.devops.plugins.job.test.jacocotest.model.JacocoTestConfig;

public interface JacocoTestConfigService {

    boolean deleteConfig(Long id);

    JacocoTestConfig getById(Long id);

    JacocoTestConfig saveConfig(JacocoTestConfig jacocoTestConfig);
}
