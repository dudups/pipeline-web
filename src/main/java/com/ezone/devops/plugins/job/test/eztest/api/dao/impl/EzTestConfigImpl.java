package com.ezone.devops.plugins.job.test.eztest.api.dao.impl;

import com.ezone.devops.plugins.job.test.eztest.api.dao.EzTestConfigDao;
import com.ezone.devops.plugins.job.test.eztest.api.model.EzTestConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class EzTestConfigImpl extends BaseCommonDao<EzTestConfig> implements EzTestConfigDao {

}
