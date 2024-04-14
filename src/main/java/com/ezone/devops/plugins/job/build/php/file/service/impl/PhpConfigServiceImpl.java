package com.ezone.devops.plugins.job.build.php.file.service.impl;

import com.ezone.devops.plugins.job.build.php.file.dao.PhpConfigDao;
import com.ezone.devops.plugins.job.build.php.file.model.PhpConfig;
import com.ezone.devops.plugins.job.build.php.file.service.PhpConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhpConfigServiceImpl implements PhpConfigService {

    @Autowired
    private PhpConfigDao phpConfigDao;

    @Override
    public PhpConfig getById(Long id) {
        return phpConfigDao.get(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return phpConfigDao.delete(id);
    }

    @Override
    public PhpConfig saveConfig(PhpConfig phpConfig) {
        return phpConfigDao.save(phpConfig);
    }
}
