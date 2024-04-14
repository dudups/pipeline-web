package com.ezone.devops.plugins.job.build.cmake.file.dao;

import com.ezone.devops.plugins.job.build.cmake.file.model.CmakeConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface CmakeConfigDao extends LongKeyBaseDao<CmakeConfig> {

    String ID = "id";
    String DATA_JSON = "data_json";
}
