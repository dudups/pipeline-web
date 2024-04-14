package com.ezone.devops.plugins.job.build.php.file.dao;

import com.ezone.devops.plugins.job.build.php.file.model.PhpConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface PhpConfigDao extends LongKeyBaseDao<PhpConfig> {

    String ID = "id";
    String DATA_JSON = "data_json";
}
