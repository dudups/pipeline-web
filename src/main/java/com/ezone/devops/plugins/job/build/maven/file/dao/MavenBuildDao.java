package com.ezone.devops.plugins.job.build.maven.file.dao;

import com.ezone.devops.plugins.job.build.maven.file.model.MavenBuild;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface MavenBuildDao extends LongKeyBaseDao<MavenBuild> {

    String ID = "id";
    String DATA_JSON = "data_json";
}
