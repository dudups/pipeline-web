package com.ezone.devops.plugins.job.test.coberturatest.service.impl;

import com.ezone.devops.plugins.job.test.coberturatest.dao.CoberturaTestConfigDao;
import com.ezone.devops.plugins.job.test.coberturatest.model.CoberturaTestConfig;
import com.ezone.devops.plugins.job.test.coberturatest.service.CoberturaTestConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CoberturaTestConfigServiceImpl implements CoberturaTestConfigService {

    @Autowired
    private CoberturaTestConfigDao coberturaTestConfigDao;

    @Override
    public boolean deleteConfig(Long id) {
        return coberturaTestConfigDao.delete(id);
    }

    @Override
    public CoberturaTestConfig getById(Long id) {
        return coberturaTestConfigDao.get(id);
    }

    @Override
    public CoberturaTestConfig saveConfig(CoberturaTestConfig coberturaTestConfig) {
        return coberturaTestConfigDao.save(coberturaTestConfig);
    }
}