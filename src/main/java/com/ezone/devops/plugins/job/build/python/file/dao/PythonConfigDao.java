package com.ezone.devops.plugins.job.build.python.file.dao;

import com.ezone.devops.plugins.job.build.python.file.model.PythonConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface PythonConfigDao extends LongKeyBaseDao<PythonConfig> {

    String ID = "id";
    String DATA_JSON = "data_json";
}
