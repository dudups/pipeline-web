package com.ezone.devops.plugins.job.scan.ezscan.service;

import com.ezone.devops.plugins.job.scan.ezscan.model.EzScanBuild;

public interface EzScanBuildService {

    EzScanBuild saveBuild(EzScanBuild ezScanBuild);

    EzScanBuild getById(Long id);

    boolean updateBuild(EzScanBuild ezScanBuild);
}
