package com.ezone.devops.plugins.job.build.maven.file.dao.impl;

import com.ezone.devops.plugins.job.build.maven.file.dao.MavenBuildDao;
import com.ezone.devops.plugins.job.build.maven.file.model.MavenBuild;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class MavenBuildDaoImpl extends BaseCommonDao<MavenBuild> implements MavenBuildDao {

}
