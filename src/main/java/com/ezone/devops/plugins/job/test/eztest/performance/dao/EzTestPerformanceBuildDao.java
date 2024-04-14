package com.ezone.devops.plugins.job.test.eztest.performance.dao;

import com.ezone.devops.plugins.job.test.eztest.performance.model.EzTestPerformanceBuild;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface EzTestPerformanceBuildDao extends LongKeyBaseDao<EzTestPerformanceBuild> {

    String ID = "id";
    String DATA_JSON = "data_json";
}
