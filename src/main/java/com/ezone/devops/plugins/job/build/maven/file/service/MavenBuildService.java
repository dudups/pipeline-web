package com.ezone.devops.plugins.job.build.maven.file.service;


import com.ezone.devops.plugins.job.build.maven.file.model.MavenBuild;

public interface MavenBuildService {

    MavenBuild getById(Long id);

    boolean deleteById(Long id);

    MavenBuild saveBuild(MavenBuild mavenBuild);

    boolean updateBuild(MavenBuild mavenBuild);

}
