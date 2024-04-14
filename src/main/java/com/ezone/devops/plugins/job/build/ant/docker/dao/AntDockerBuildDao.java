package com.ezone.devops.plugins.job.build.ant.docker.dao;

import com.ezone.devops.plugins.job.build.ant.docker.model.AntDockerBuild;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface AntDockerBuildDao extends LongKeyBaseDao<AntDockerBuild> {

    String ID = "id";
    String DATA_JSON = "data_json";
}
