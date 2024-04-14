package com.ezone.devops.plugins.job.other.shell.service.impl;

import com.ezone.devops.plugins.job.other.shell.dao.ShellExecutorConfigDao;
import com.ezone.devops.plugins.job.other.shell.model.ShellExecutorConfig;
import com.ezone.devops.plugins.job.other.shell.service.ShellExecutorConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ShellExecutorConfigServiceImpl implements ShellExecutorConfigService {

    @Autowired
    private ShellExecutorConfigDao shellExecutorConfigDao;

    @Override
    public boolean deleteShellExecutorConfig(Long id) {
        return shellExecutorConfigDao.delete(id);
    }

    @Override
    public ShellExecutorConfig getById(Long id) {
        return shellExecutorConfigDao.get(id);
    }

    @Override
    public ShellExecutorConfig saveShellExecutorConfig(ShellExecutorConfig shellExecutorConfig) {
        return shellExecutorConfigDao.save(shellExecutorConfig);
    }
}