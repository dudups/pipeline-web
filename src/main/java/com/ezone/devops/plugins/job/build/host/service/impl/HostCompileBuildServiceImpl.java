package com.ezone.devops.plugins.job.build.host.service.impl;

import com.ezone.devops.plugins.job.build.host.dao.HostCompileBuildDao;
import com.ezone.devops.plugins.job.build.host.model.HostCompileBuild;
import com.ezone.devops.plugins.job.build.host.service.HostCompileBuildService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HostCompileBuildServiceImpl implements HostCompileBuildService {

    @Autowired
    private HostCompileBuildDao hostCompileBuildDao;

    @Override
    public HostCompileBuild getById(Long id) {
        return hostCompileBuildDao.get(id);
    }

    @Override
    public void saveBuild(HostCompileBuild hostCompileBuild) {
        hostCompileBuildDao.save(hostCompileBuild);
    }

    @Override
    public boolean updateBuild(HostCompileBuild hostCompileBuild) {
        return hostCompileBuildDao.update(hostCompileBuild);
    }

}
