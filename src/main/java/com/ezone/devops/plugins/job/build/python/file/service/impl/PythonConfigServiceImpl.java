package com.ezone.devops.plugins.job.build.python.file.service.impl;

import com.ezone.devops.plugins.job.build.python.file.dao.PythonConfigDao;
import com.ezone.devops.plugins.job.build.python.file.model.PythonConfig;
import com.ezone.devops.plugins.job.build.python.file.service.PythonConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PythonConfigServiceImpl implements PythonConfigService {

    @Autowired
    private PythonConfigDao pythonConfigDao;

    @Override
    public PythonConfig getById(Long id) {
        return pythonConfigDao.get(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return pythonConfigDao.delete(id);
    }

    @Override
    public PythonConfig saveConfig(PythonConfig pythonConfig) {
        return pythonConfigDao.save(pythonConfig);
    }
}
