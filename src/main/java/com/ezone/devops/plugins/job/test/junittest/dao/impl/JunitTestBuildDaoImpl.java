package com.ezone.devops.plugins.job.test.junittest.dao.impl;

import com.ezone.devops.plugins.job.test.junittest.dao.JunitTestBuildDao;
import com.ezone.devops.plugins.job.test.junittest.model.JunitTestBuild;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class JunitTestBuildDaoImpl extends BaseCommonDao<JunitTestBuild> implements JunitTestBuildDao {

}
