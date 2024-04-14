package com.ezone.devops.plugins.job.build.go.docker.dao.impl;

import com.ezone.devops.plugins.job.build.go.docker.dao.GoDockerConfigDao;
import com.ezone.devops.plugins.job.build.go.docker.model.GoDockerConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class GoDockerConfigDaoImpl extends BaseCommonDao<GoDockerConfig> implements GoDockerConfigDao {

}
