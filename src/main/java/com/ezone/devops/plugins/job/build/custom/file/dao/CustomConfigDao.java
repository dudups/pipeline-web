package com.ezone.devops.plugins.job.build.custom.file.dao;

import com.ezone.devops.plugins.job.build.custom.file.model.CustomConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface CustomConfigDao extends LongKeyBaseDao<CustomConfig> {

    String ID = "id";
    String DATA_JSON = "data_json";
}
