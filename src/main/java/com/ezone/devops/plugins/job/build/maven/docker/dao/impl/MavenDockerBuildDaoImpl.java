package com.ezone.devops.plugins.job.build.maven.docker.dao.impl;

import com.ezone.devops.plugins.job.build.maven.docker.dao.MavenDockerBuildDao;
import com.ezone.devops.plugins.job.build.maven.docker.model.MavenDockerBuild;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class MavenDockerBuildDaoImpl extends BaseCommonDao<MavenDockerBuild> implements MavenDockerBuildDao {

}
