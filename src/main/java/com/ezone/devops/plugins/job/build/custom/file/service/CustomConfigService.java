package com.ezone.devops.plugins.job.build.custom.file.service;


import com.ezone.devops.plugins.job.build.custom.file.model.CustomConfig;

public interface CustomConfigService {

    CustomConfig getById(Long id);

    boolean deleteById(Long id);

    CustomConfig saveConfig(CustomConfig customConfig);

}
