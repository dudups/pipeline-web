package com.ezone.devops.plugins.job.build.ant.file.service;


import com.ezone.devops.plugins.job.build.ant.file.model.AntConfig;

public interface AntConfigService {

    AntConfig getById(Long id);

    boolean deleteById(Long id);

    AntConfig saveConfig(AntConfig antConfig);

}
