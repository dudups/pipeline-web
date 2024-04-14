package com.ezone.devops.plugins.job.build.maven.file.service;


import com.ezone.devops.plugins.job.build.maven.file.model.MavenConfig;

public interface MavenConfigService {

    MavenConfig getById(Long id);

    boolean deleteById(Long id);

    MavenConfig saveConfig(MavenConfig mavenConfig);

}
