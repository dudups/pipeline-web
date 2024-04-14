package com.ezone.devops.plugins.job.build.python.docker.dao.impl;

import com.ezone.devops.plugins.job.build.python.docker.dao.PythonDockerConfigDao;
import com.ezone.devops.plugins.job.build.python.docker.model.PythonDockerConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class PythonDockerConfigDaoImpl extends BaseCommonDao<PythonDockerConfig> implements PythonDockerConfigDao {

}
