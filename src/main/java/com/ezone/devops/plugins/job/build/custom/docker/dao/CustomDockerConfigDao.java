package com.ezone.devops.plugins.job.build.custom.docker.dao;

import com.ezone.devops.plugins.job.build.custom.docker.model.CustomDockerConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface CustomDockerConfigDao extends LongKeyBaseDao<CustomDockerConfig> {

    String ID = "id";
    String DATA_JSON = "data_json";
}
