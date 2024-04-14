package com.ezone.devops.plugins.job.test.maventest.dao.impl;

import com.ezone.devops.plugins.job.test.maventest.dao.MavenTestConfigDao;
import com.ezone.devops.plugins.job.test.maventest.model.MavenTestConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class MavenTestConfigImpl extends BaseCommonDao<MavenTestConfig> implements MavenTestConfigDao {

}
