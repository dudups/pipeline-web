package com.ezone.devops.plugins.job.build.cmake.file.service;


import com.ezone.devops.plugins.job.build.cmake.file.model.CmakeConfig;

public interface CmakeConfigService {

    CmakeConfig getById(Long id);

    boolean deleteById(Long id);

    CmakeConfig saveConfig(CmakeConfig cmakeConfig);

}
