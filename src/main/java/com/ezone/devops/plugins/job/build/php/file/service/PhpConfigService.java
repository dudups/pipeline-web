package com.ezone.devops.plugins.job.build.php.file.service;


import com.ezone.devops.plugins.job.build.php.file.model.PhpConfig;

public interface PhpConfigService {

    PhpConfig getById(Long id);

    boolean deleteById(Long id);

    PhpConfig saveConfig(PhpConfig phpConfig);

}
