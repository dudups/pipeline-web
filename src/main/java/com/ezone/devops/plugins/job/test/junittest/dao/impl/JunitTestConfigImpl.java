package com.ezone.devops.plugins.job.test.junittest.dao.impl;

import com.ezone.devops.plugins.job.test.junittest.dao.JunitTestConfigDao;
import com.ezone.devops.plugins.job.test.junittest.model.JunitTestConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class JunitTestConfigImpl extends BaseCommonDao<JunitTestConfig> implements JunitTestConfigDao {

}
