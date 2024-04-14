package com.ezone.devops.plugins.job.build.php.docker.dao;

import com.ezone.devops.plugins.job.build.php.docker.model.PhpDockerConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface PhpDockerConfigDao extends LongKeyBaseDao<PhpDockerConfig> {

    String ID = "id";
    String DATA_JSON = "data_json";
}
