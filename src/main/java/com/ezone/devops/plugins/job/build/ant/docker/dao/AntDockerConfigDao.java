package com.ezone.devops.plugins.job.build.ant.docker.dao;

import com.ezone.devops.plugins.job.build.ant.docker.model.AntDockerConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface AntDockerConfigDao extends LongKeyBaseDao<AntDockerConfig> {

    String ID = "id";
    String DATA_JSON = "data_json";
}
