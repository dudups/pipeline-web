package com.ezone.devops.plugins.job.build.ant.file.service;


import com.ezone.devops.plugins.job.build.ant.file.model.AntBuild;

public interface AntBuildService {

    AntBuild getById(Long id);

    boolean deleteById(Long id);

    AntBuild saveBuild(AntBuild antBuild);

    boolean updateBuild(AntBuild antBuild);
}
