package com.ezone.devops.plugins.job.build.cmake.docker.dao;

import com.ezone.devops.plugins.job.build.cmake.docker.model.CmakeDockerConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface CmakeDockerConfigDao extends LongKeyBaseDao<CmakeDockerConfig> {

    String ID = "id";
    String DATA_JSON = "data_json";
}
