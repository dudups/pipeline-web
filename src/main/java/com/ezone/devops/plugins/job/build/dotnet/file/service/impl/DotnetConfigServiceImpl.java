package com.ezone.devops.plugins.job.build.dotnet.file.service.impl;

import com.ezone.devops.plugins.job.build.dotnet.file.dao.DotnetConfigDao;
import com.ezone.devops.plugins.job.build.dotnet.file.model.DotnetConfig;
import com.ezone.devops.plugins.job.build.dotnet.file.service.DotnetConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DotnetConfigServiceImpl implements DotnetConfigService {

    @Autowired
    private DotnetConfigDao dotnetConfigDao;

    @Override
    public DotnetConfig getById(Long id) {
        return dotnetConfigDao.get(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return dotnetConfigDao.delete(id);
    }

    @Override
    public DotnetConfig saveConfig(DotnetConfig dotnetConfig) {
        return dotnetConfigDao.save(dotnetConfig);
    }
}
