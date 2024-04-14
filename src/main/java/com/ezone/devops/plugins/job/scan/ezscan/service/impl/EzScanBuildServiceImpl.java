package com.ezone.devops.plugins.job.scan.ezscan.service.impl;

import com.ezone.devops.plugins.job.scan.ezscan.dao.EzScanBuildDao;
import com.ezone.devops.plugins.job.scan.ezscan.model.EzScanBuild;
import com.ezone.devops.plugins.job.scan.ezscan.service.EzScanBuildService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EzScanBuildServiceImpl implements EzScanBuildService {

    @Autowired
    private EzScanBuildDao ezScanBuildDao;

    @Override
    public boolean updateBuild(EzScanBuild ezScanBuild) {
        return ezScanBuildDao.update(ezScanBuild);
    }

    @Override
    public EzScanBuild getById(Long id) {
        return ezScanBuildDao.get(id);
    }

    @Override
    public EzScanBuild saveBuild(EzScanBuild ezScanBuild) {
        return ezScanBuildDao.save(ezScanBuild);
    }
}
