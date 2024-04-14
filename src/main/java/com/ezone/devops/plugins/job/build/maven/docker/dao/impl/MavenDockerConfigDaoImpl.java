package com.ezone.devops.plugins.job.build.maven.docker.dao.impl;

import com.ezone.devops.plugins.job.build.maven.docker.dao.MavenDockerConfigDao;
import com.ezone.devops.plugins.job.build.maven.docker.model.MavenDockerConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class MavenDockerConfigDaoImpl extends BaseCommonDao<MavenDockerConfig> implements MavenDockerConfigDao {

}
