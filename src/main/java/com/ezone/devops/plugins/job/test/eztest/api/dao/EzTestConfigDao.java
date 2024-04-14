package com.ezone.devops.plugins.job.test.eztest.api.dao;

import com.ezone.devops.plugins.job.test.eztest.api.model.EzTestConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface EzTestConfigDao extends LongKeyBaseDao<EzTestConfig> {

    String ID = "id";
    String ENV_ID = "env_id";
    String SUITE_ID = "suite_id";
    String PL_CONF_ID = "pl_conf_id";
}
