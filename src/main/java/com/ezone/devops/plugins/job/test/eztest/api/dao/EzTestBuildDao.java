package com.ezone.devops.plugins.job.test.eztest.api.dao;

import com.ezone.devops.plugins.job.test.eztest.api.model.EzTestBuild;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface EzTestBuildDao extends LongKeyBaseDao<EzTestBuild> {

    String ID = "id";
    String PLAN_ID = "plan_id";
    String SPACE_KEY = "space_key";
}
