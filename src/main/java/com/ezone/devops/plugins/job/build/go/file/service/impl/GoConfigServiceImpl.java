package com.ezone.devops.plugins.job.build.go.file.service.impl;

import com.ezone.devops.plugins.job.build.go.file.dao.GoConfigDao;
import com.ezone.devops.plugins.job.build.go.file.model.GoConfig;
import com.ezone.devops.plugins.job.build.go.file.service.GoConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoConfigServiceImpl implements GoConfigService {

    @Autowired
    private GoConfigDao goConfigDao;

    @Override
    public GoConfig getById(Long id) {
        return goConfigDao.get(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return goConfigDao.delete(id);
    }

    @Override
    public GoConfig saveConfig(GoConfig goConfig) {
        return goConfigDao.save(goConfig);
    }
}
