package com.ezone.devops.plugins.job.test.maventest.dao.impl;

import com.ezone.devops.plugins.job.test.maventest.dao.MavenTestBuildDao;
import com.ezone.devops.plugins.job.test.maventest.model.MavenTestBuild;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class MavenTestBuildDaoImpl extends BaseCommonDao<MavenTestBuild> implements MavenTestBuildDao {

}
