package com.ezone.devops.plugins.job.build.gradle.docker.dao;

import com.ezone.devops.plugins.job.build.gradle.docker.model.GradleDockerBuild;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface GradleDockerBuildDao extends LongKeyBaseDao<GradleDockerBuild> {

    String ID = "id";
    String DATA_JSON = "data_json";
}
