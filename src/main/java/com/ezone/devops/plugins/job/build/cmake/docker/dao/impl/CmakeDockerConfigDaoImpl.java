package com.ezone.devops.plugins.job.build.cmake.docker.dao.impl;

import com.ezone.devops.plugins.job.build.cmake.docker.dao.CmakeDockerConfigDao;
import com.ezone.devops.plugins.job.build.cmake.docker.model.CmakeDockerConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class CmakeDockerConfigDaoImpl extends BaseCommonDao<CmakeDockerConfig> implements CmakeDockerConfigDao {

}
