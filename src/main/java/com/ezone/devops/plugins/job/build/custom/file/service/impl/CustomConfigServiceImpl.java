package com.ezone.devops.plugins.job.build.custom.file.service.impl;

import com.ezone.devops.plugins.job.build.custom.file.dao.CustomConfigDao;
import com.ezone.devops.plugins.job.build.custom.file.model.CustomConfig;
import com.ezone.devops.plugins.job.build.custom.file.service.CustomConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomConfigServiceImpl implements CustomConfigService {

    @Autowired
    private CustomConfigDao customConfigDao;

    @Override
    public CustomConfig getById(Long id) {
        return customConfigDao.get(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return customConfigDao.delete(id);
    }

    @Override
    public CustomConfig saveConfig(CustomConfig customConfig) {
        return customConfigDao.save(customConfig);
    }
}
