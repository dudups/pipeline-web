package com.ezone.devops.plugins.job.test.jacocotest.dao.impl;

import com.ezone.devops.plugins.job.test.jacocotest.dao.JacocoTestBuildDao;
import com.ezone.devops.plugins.job.test.jacocotest.model.JacocoTestBuild;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class JacocoTestBuildDaoImpl extends BaseCommonDao<JacocoTestBuild> implements JacocoTestBuildDao {

}
