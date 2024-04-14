package com.ezone.devops.plugins.job.deploy.ezk8s.service.impl;

import com.ezone.devops.plugins.job.deploy.ezk8s.dao.Ezk8sDeployConfigDao;
import com.ezone.devops.plugins.job.deploy.ezk8s.model.Ezk8sDeployConfig;
import com.ezone.devops.plugins.job.deploy.ezk8s.service.Ezk8sDeployConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Ezk8SDeployConfigServiceImpl implements Ezk8sDeployConfigService {

    @Autowired
    private Ezk8sDeployConfigDao ezk8SDeployConfigDao;

    @Override
    public Ezk8sDeployConfig findById(Long id) {
        return ezk8SDeployConfigDao.get(id);
    }

    @Override
    public void saveConfig(Ezk8sDeployConfig ezk8SDeployConfig) {
        ezk8SDeployConfigDao.save(ezk8SDeployConfig);
    }

    @Override
    public boolean deleteConfig(Long id) {
        return ezk8SDeployConfigDao.delete(id);
    }
}
