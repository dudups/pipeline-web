package com.ezone.devops.plugins.job.build.dotnet.docker.dao;

import com.ezone.devops.plugins.job.build.dotnet.docker.model.DotnetDockerConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface DotnetDockerConfigDao extends LongKeyBaseDao<DotnetDockerConfig> {

    String ID = "id";
    String DATA_JSON = "data_json";
}
