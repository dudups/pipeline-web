package com.ezone.devops.plugins.job.build.dotnet.docker.dao.impl;

import com.ezone.devops.plugins.job.build.dotnet.docker.dao.DotnetDockerConfigDao;
import com.ezone.devops.plugins.job.build.dotnet.docker.model.DotnetDockerConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class DotnetDockerConfigDaoImpl extends BaseCommonDao<DotnetDockerConfig> implements DotnetDockerConfigDao {

}
