package com.ezone.devops.plugins.job.test.eztest.performance.service;

import com.ezone.devops.plugins.job.test.eztest.performance.model.EzTestPerformanceBuild;

public interface EzTestPerformanceBuildService {

    EzTestPerformanceBuild saveBuild(EzTestPerformanceBuild ezTestPerformanceBuild);

    EzTestPerformanceBuild getById(Long id);

    boolean updateBuild(EzTestPerformanceBuild ezTestPerformanceBuild);
}
