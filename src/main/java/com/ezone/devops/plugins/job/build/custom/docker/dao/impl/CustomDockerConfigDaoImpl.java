package com.ezone.devops.plugins.job.build.custom.docker.dao.impl;

import com.ezone.devops.plugins.job.build.custom.docker.dao.CustomDockerConfigDao;
import com.ezone.devops.plugins.job.build.custom.docker.model.CustomDockerConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class CustomDockerConfigDaoImpl extends BaseCommonDao<CustomDockerConfig> implements CustomDockerConfigDao {

}
