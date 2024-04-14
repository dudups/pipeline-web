package com.ezone.devops.plugins.job.build.maven.docker.dao;

import com.ezone.devops.plugins.job.build.maven.docker.model.MavenDockerBuild;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface MavenDockerBuildDao extends LongKeyBaseDao<MavenDockerBuild> {

    String ID = "id";
    String DATA_JSON = "data_json";
}
