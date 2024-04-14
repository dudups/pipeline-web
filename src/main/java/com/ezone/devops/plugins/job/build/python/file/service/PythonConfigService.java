package com.ezone.devops.plugins.job.build.python.file.service;


import com.ezone.devops.plugins.job.build.python.file.model.PythonConfig;

public interface PythonConfigService {

    PythonConfig getById(Long id);

    boolean deleteById(Long id);

    PythonConfig saveConfig(PythonConfig pythonConfig);

}
