package com.ezone.devops.plugins.job.build.maven.file.dao.impl;

import com.ezone.devops.plugins.job.build.maven.file.dao.MavenConfigDao;
import com.ezone.devops.plugins.job.build.maven.file.model.MavenConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class MavenConfigDaoImpl extends BaseCommonDao<MavenConfig> implements MavenConfigDao {

}
