package com.ezone.devops.plugins.job.build.host.service.impl;

import com.ezone.devops.plugins.job.build.host.dao.HostCompileConfigDao;
import com.ezone.devops.plugins.job.build.host.model.HostCompileConfig;
import com.ezone.devops.plugins.job.build.host.service.HostCompileConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class HostCompileConfigServiceImpl implements HostCompileConfigService {

    @Autowired
    private HostCompileConfigDao hostCompileConfigDao;

    @Override
    public HostCompileConfig getById(Long id) {
        return hostCompileConfigDao.get(id);
    }

    @Override
    public List<HostCompileConfig> findByIds(Collection<Long> ids) {
        return hostCompileConfigDao.get(ids);
    }

    @Override
    public boolean deleteById(Long id) {
        return hostCompileConfigDao.delete(id);
    }

    @Override
    public void saveConfig(HostCompileConfig hostCompileConfig) {
        hostCompileConfigDao.save(hostCompileConfig);
    }
}
