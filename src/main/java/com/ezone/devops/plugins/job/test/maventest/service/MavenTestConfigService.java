package com.ezone.devops.plugins.job.test.maventest.service;

import com.ezone.devops.plugins.job.test.maventest.model.MavenTestConfig;

public interface MavenTestConfigService {

    boolean deleteConfig(Long id);

    MavenTestConfig getById(Long id);

    MavenTestConfig saveConfig(MavenTestConfig mavenTestConfig);
}
