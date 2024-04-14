package com.ezone.devops.plugins.job.test.coberturatest.service;

import com.ezone.devops.plugins.job.test.coberturatest.model.CoberturaTestConfig;

public interface CoberturaTestConfigService {

    boolean deleteConfig(Long id);

    CoberturaTestConfig getById(Long id);

    CoberturaTestConfig saveConfig(CoberturaTestConfig coberturaTestConfig);
}
