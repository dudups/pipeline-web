package com.ezone.devops.plugins.job.build.maven.docker.dao;

import com.ezone.devops.plugins.job.build.maven.docker.model.MavenDockerConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface MavenDockerConfigDao extends LongKeyBaseDao<MavenDockerConfig> {

    String ID = "id";
    String DATA_JSON = "data_json";
}
