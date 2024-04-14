package com.ezone.devops.plugins.job.test.coberturatest.service;

import com.ezone.devops.plugins.job.test.coberturatest.model.CoberturaTestBuild;

public interface CoberturaTestBuildService {

    CoberturaTestBuild saveBuild(CoberturaTestBuild mavenTestBuild);

    CoberturaTestBuild getById(Long id);

    boolean updateBuild(CoberturaTestBuild mavenTestBuild);
}
