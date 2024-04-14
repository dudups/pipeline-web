package com.ezone.devops.plugins.job.build.ant.file.dao;

import com.ezone.devops.plugins.job.build.ant.file.model.AntBuild;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface AntBuildDao extends LongKeyBaseDao<AntBuild> {

    String ID = "id";
    String DATA_JSON = "data_json";
}
