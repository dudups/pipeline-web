package com.ezone.devops.plugins.job.test.eztest.performance.dao;

import com.ezone.devops.plugins.job.test.eztest.performance.model.EzTestPerformanceConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface EzTestPerformanceConfigDao extends LongKeyBaseDao<EzTestPerformanceConfig> {

    String ID = "id";
    String DATA_JSON = "data_json";
}
