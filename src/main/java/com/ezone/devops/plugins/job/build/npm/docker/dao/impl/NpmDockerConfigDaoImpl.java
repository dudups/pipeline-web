package com.ezone.devops.plugins.job.build.npm.docker.dao.impl;

import com.ezone.devops.plugins.job.build.npm.docker.dao.NpmDockerConfigDao;
import com.ezone.devops.plugins.job.build.npm.docker.model.NpmDockerConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class NpmDockerConfigDaoImpl extends BaseCommonDao<NpmDockerConfig> implements NpmDockerConfigDao {

}
