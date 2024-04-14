package com.ezone.devops.plugins.job.build.maven.docker.service;


import com.ezone.devops.plugins.job.build.maven.docker.model.MavenDockerBuild;

public interface MavenDockerBuildService {

    MavenDockerBuild getById(Long id);

    boolean deleteById(Long id);

    MavenDockerBuild saveBuild(MavenDockerBuild mavenDockerBuild);

    boolean updateBuild(MavenDockerBuild mavenDockerBuild);
}
