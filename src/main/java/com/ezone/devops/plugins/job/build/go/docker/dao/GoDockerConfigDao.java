package com.ezone.devops.plugins.job.build.go.docker.dao;

import com.ezone.devops.plugins.job.build.go.docker.model.GoDockerConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface GoDockerConfigDao extends LongKeyBaseDao<GoDockerConfig> {

    String ID = "id";
    String DATA_JSON = "data_json";
}
