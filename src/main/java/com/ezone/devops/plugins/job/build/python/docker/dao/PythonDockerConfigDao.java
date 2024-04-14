package com.ezone.devops.plugins.job.build.python.docker.dao;

import com.ezone.devops.plugins.job.build.python.docker.model.PythonDockerConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface PythonDockerConfigDao extends LongKeyBaseDao<PythonDockerConfig> {

    String ID = "id";
    String DATA_JSON = "data_json";
}
