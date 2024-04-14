package com.ezone.devops.plugins.job.build.ant.docker.dao.impl;

import com.ezone.devops.plugins.job.build.ant.docker.dao.AntDockerConfigDao;
import com.ezone.devops.plugins.job.build.ant.docker.model.AntDockerConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class AntDockerConfigDaoImpl extends BaseCommonDao<AntDockerConfig> implements AntDockerConfigDao {

}
