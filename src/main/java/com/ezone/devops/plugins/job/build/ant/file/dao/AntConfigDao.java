package com.ezone.devops.plugins.job.build.ant.file.dao;

import com.ezone.devops.plugins.job.build.ant.file.model.AntConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface AntConfigDao extends LongKeyBaseDao<AntConfig> {

    String ID = "id";
    String DATA_JSON = "data_json";
}
