package com.ezone.devops.plugins.job.build.gradle.docker.dao;

import com.ezone.devops.plugins.job.build.gradle.docker.model.GradleDockerConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface GradleDockerConfigDao extends LongKeyBaseDao<GradleDockerConfig> {

    String ID = "id";
    String DATA_JSON = "data_json";
}
