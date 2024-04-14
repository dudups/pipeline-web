package com.ezone.devops.plugins.job.test.coberturatest.service.impl;

import com.ezone.devops.plugins.job.test.coberturatest.dao.CoberturaTestBuildDao;
import com.ezone.devops.plugins.job.test.coberturatest.model.CoberturaTestBuild;
import com.ezone.devops.plugins.job.test.coberturatest.service.CoberturaTestBuildService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CoberturaTestBuildServiceImpl implements CoberturaTestBuildService {

    @Autowired
    private CoberturaTestBuildDao coberturaTestBuildDao;

    @Override
    public boolean updateBuild(CoberturaTestBuild mavenTestBuild) {
        return coberturaTestBuildDao.update(mavenTestBuild);
    }

    @Override
    public CoberturaTestBuild getById(Long id) {
        return coberturaTestBuildDao.get(id);
    }

    @Override
    public CoberturaTestBuild saveBuild(CoberturaTestBuild mavenTestBuild) {
        return coberturaTestBuildDao.save(mavenTestBuild);
    }
}
