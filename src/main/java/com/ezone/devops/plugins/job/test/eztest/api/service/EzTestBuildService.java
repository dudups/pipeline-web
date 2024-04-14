package com.ezone.devops.plugins.job.test.eztest.api.service;

import com.ezone.devops.plugins.job.test.eztest.api.model.EzTestBuild;

public interface EzTestBuildService {

    EzTestBuild saveBuild(EzTestBuild ezTestBuild);

    EzTestBuild getById(Long id);

    boolean updateBuild(EzTestBuild ezTestBuild);
}
