package com.ezone.devops.plugins.job.build.npm.docker.dao;

import com.ezone.devops.plugins.job.build.npm.docker.model.NpmDockerConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface NpmDockerConfigDao extends LongKeyBaseDao<NpmDockerConfig> {

    String ID = "id";
    String DATA_JSON = "data_json";
}
