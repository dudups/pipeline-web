package com.ezone.devops.plugins.job.test.eztest.api.service.impl;

import com.ezone.devops.plugins.job.test.eztest.api.dao.EzTestBuildDao;
import com.ezone.devops.plugins.job.test.eztest.api.model.EzTestBuild;
import com.ezone.devops.plugins.job.test.eztest.api.service.EzTestBuildService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EzTestBuildServiceImpl implements EzTestBuildService {

    @Autowired
    private EzTestBuildDao ezTestBuildDao;

    @Override
    public boolean updateBuild(EzTestBuild ezTestBuild) {
        return ezTestBuildDao.update(ezTestBuild);
    }

    @Override
    public EzTestBuild getById(Long id) {
        return ezTestBuildDao.get(id);
    }

    @Override
    public EzTestBuild saveBuild(EzTestBuild ezTestBuild) {
        return ezTestBuildDao.save(ezTestBuild);
    }
}
