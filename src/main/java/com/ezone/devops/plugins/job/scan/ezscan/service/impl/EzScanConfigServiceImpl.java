package com.ezone.devops.plugins.job.scan.ezscan.service.impl;

import com.ezone.devops.plugins.job.scan.ezscan.dao.EzScanConfigDao;
import com.ezone.devops.plugins.job.scan.ezscan.model.EzScanConfig;
import com.ezone.devops.plugins.job.scan.ezscan.service.EzScanConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class EzScanConfigServiceImpl implements EzScanConfigService {

    @Autowired
    private EzScanConfigDao ezScanConfigDao;

    @Override
    public List<EzScanConfig> getAll() {
        return ezScanConfigDao.findAll();
    }

    @Override
    public boolean deleteConfig(Long id) {
        return ezScanConfigDao.delete(id);
    }

    @Override
    public EzScanConfig getById(Long id) {
        return ezScanConfigDao.get(id);
    }

    @Override
    public EzScanConfig saveConfig(EzScanConfig ezScanConfig) {
        return ezScanConfigDao.save(ezScanConfig);
    }

    @Override
    public boolean updateConfig(EzScanConfig ezScanConfig) {
        return ezScanConfigDao.update(ezScanConfig);
    }
}