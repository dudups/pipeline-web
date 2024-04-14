package com.ezone.devops.plugins.job.build.python.docker.service;


import com.ezone.devops.plugins.job.build.python.docker.model.PythonDockerConfig;

public interface PythonDockerConfigService {

    PythonDockerConfig getById(Long id);

    boolean deleteById(Long id);

    PythonDockerConfig saveConfig(PythonDockerConfig pythonDockerConfig);

}
