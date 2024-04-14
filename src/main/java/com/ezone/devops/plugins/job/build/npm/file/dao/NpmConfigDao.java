package com.ezone.devops.plugins.job.build.npm.file.dao;

import com.ezone.devops.plugins.job.build.npm.file.model.NpmConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface NpmConfigDao extends LongKeyBaseDao<NpmConfig> {

    String ID = "id";
    String DATA_JSON = "data_json";
}
