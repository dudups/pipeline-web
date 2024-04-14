package com.ezone.devops.plugins.job.build.dotnet.file.dao;

import com.ezone.devops.plugins.job.build.dotnet.file.model.DotnetConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface DotnetConfigDao extends LongKeyBaseDao<DotnetConfig> {

    String ID = "id";
    String DATA_JSON = "data_json";
}
