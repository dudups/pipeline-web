package com.ezone.devops.plugins.job.build.npm.file.service.impl;

import com.ezone.devops.plugins.job.build.npm.file.dao.NpmConfigDao;
import com.ezone.devops.plugins.job.build.npm.file.model.NpmConfig;
import com.ezone.devops.plugins.job.build.npm.file.service.NpmConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NpmConfigServiceImpl implements NpmConfigService {

    @Autowired
    private NpmConfigDao npmConfigDao;

    @Override
    public NpmConfig getById(Long id) {
        return npmConfigDao.get(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return npmConfigDao.delete(id);
    }

    @Override
    public NpmConfig saveConfig(NpmConfig npmConfig) {
        return npmConfigDao.save(npmConfig);
    }
}
