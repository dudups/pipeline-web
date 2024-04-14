package com.ezone.devops.plugins.job.build.ant.file.service.impl;

import com.ezone.devops.plugins.job.build.ant.file.dao.AntConfigDao;
import com.ezone.devops.plugins.job.build.ant.file.model.AntConfig;
import com.ezone.devops.plugins.job.build.ant.file.service.AntConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AntConfigServiceImpl implements AntConfigService {

    @Autowired
    private AntConfigDao antConfigDao;

    @Override
    public AntConfig getById(Long id) {
        return antConfigDao.get(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return antConfigDao.delete(id);
    }

    @Override
    public AntConfig saveConfig(AntConfig antConfig) {
        return antConfigDao.save(antConfig);
    }
}
