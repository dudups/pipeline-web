package com.ezone.devops.plugins.job.build.php.docker.service;


import com.ezone.devops.plugins.job.build.php.docker.model.PhpDockerConfig;

public interface PhpDockerConfigService {

    PhpDockerConfig getById(Long id);

    boolean deleteById(Long id);

    PhpDockerConfig saveConfig(PhpDockerConfig phpDockerConfig);

}
