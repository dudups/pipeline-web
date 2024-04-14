package com.ezone.devops.plugins.job.test.jacocotest.dao.impl;

import com.ezone.devops.plugins.job.test.jacocotest.dao.JacocoTestConfigDao;
import com.ezone.devops.plugins.job.test.jacocotest.model.JacocoTestConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class JacocoTestConfigImpl extends BaseCommonDao<JacocoTestConfig> implements JacocoTestConfigDao {

}
