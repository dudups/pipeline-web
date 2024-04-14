package com.ezone.devops.plugins.job.scan.ezscan.service;

import com.ezone.devops.plugins.job.scan.ezscan.model.EzScanConfig;

import java.util.List;

public interface EzScanConfigService {

    List<EzScanConfig> getAll();

    boolean deleteConfig(Long id);

    EzScanConfig getById(Long id);

    EzScanConfig saveConfig(EzScanConfig ezScanConfig);

    boolean updateConfig(EzScanConfig ezScanConfig);
}
