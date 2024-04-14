package com.ezone.devops.plugins.job.build.go.file.service;


import com.ezone.devops.plugins.job.build.go.file.model.GoConfig;

public interface GoConfigService {

    GoConfig getById(Long id);

    boolean deleteById(Long id);

    GoConfig saveConfig(GoConfig goConfig);

}
