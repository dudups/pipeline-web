package com.ezone.devops.plugins.job.build.cmake.file.service.impl;

import com.ezone.devops.plugins.job.build.cmake.file.dao.CmakeConfigDao;
import com.ezone.devops.plugins.job.build.cmake.file.model.CmakeConfig;
import com.ezone.devops.plugins.job.build.cmake.file.service.CmakeConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CmakeConfigServiceImpl implements CmakeConfigService {

    @Autowired
    private CmakeConfigDao cmakeConfigDao;

    @Override
    public CmakeConfig getById(Long id) {
        return cmakeConfigDao.get(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return cmakeConfigDao.delete(id);
    }

    @Override
    public CmakeConfig saveConfig(CmakeConfig cmakeConfig) {
        return cmakeConfigDao.save(cmakeConfig);
    }
}
