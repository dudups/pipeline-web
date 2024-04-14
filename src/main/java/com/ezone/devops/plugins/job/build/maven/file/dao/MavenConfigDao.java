package com.ezone.devops.plugins.job.build.maven.file.dao;

import com.ezone.devops.plugins.job.build.maven.file.model.MavenConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface MavenConfigDao extends LongKeyBaseDao<MavenConfig> {

    String ID = "id";
    String DATA_JSON = "data_json";
}
