package com.ezone.devops.plugins.job.build.dotnet.file.service;


import com.ezone.devops.plugins.job.build.dotnet.file.model.DotnetConfig;

public interface DotnetConfigService {

    DotnetConfig getById(Long id);

    boolean deleteById(Long id);

    DotnetConfig saveConfig(DotnetConfig dotnetConfig);

}
