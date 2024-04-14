package com.ezone.devops.plugins.job.build.npm.file.service;


import com.ezone.devops.plugins.job.build.npm.file.model.NpmConfig;

public interface NpmConfigService {

    NpmConfig getById(Long id);

    boolean deleteById(Long id);

    NpmConfig saveConfig(NpmConfig npmConfig);

}
