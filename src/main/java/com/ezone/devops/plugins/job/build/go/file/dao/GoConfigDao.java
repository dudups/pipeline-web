package com.ezone.devops.plugins.job.build.go.file.dao;

import com.ezone.devops.plugins.job.build.go.file.model.GoConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface GoConfigDao extends LongKeyBaseDao<GoConfig> {

    String ID = "id";
    String DATA_JSON = "data_json";
}
