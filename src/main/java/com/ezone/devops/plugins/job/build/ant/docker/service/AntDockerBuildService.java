package com.ezone.devops.plugins.job.build.ant.docker.service;


import com.ezone.devops.plugins.job.build.ant.docker.model.AntDockerBuild;

public interface AntDockerBuildService {

    AntDockerBuild getById(Long id);

    boolean deleteById(Long id);

    AntDockerBuild saveBuild(AntDockerBuild antDockerBuild);

    boolean updateBuild(AntDockerBuild antDockerBuild);
}
