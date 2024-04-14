package com.ezone.devops.plugins.job.test.eztest.performance.service.impl;

import com.ezone.devops.plugins.job.test.eztest.performance.dao.EzTestPerformanceBuildDao;
import com.ezone.devops.plugins.job.test.eztest.performance.model.EzTestPerformanceBuild;
import com.ezone.devops.plugins.job.test.eztest.performance.service.EzTestPerformanceBuildService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EzTestPerformanceBuildServiceImpl implements EzTestPerformanceBuildService {

    @Autowired
    private EzTestPerformanceBuildDao ezTestPerformanceBuildDao;

    @Override
    public boolean updateBuild(EzTestPerformanceBuild ezTestPerformanceBuild) {
        return ezTestPerformanceBuildDao.update(ezTestPerformanceBuild);
    }

    @Override
    public EzTestPerformanceBuild getById(Long id) {
        return ezTestPerformanceBuildDao.get(id);
    }

    @Override
    public EzTestPerformanceBuild saveBuild(EzTestPerformanceBuild ezTestPerformanceBuild) {
        return ezTestPerformanceBuildDao.save(ezTestPerformanceBuild);
    }
}
