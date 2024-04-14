package com.ezone.devops.plugins.job.other.docker.dao.impl;

import com.ezone.devops.plugins.job.other.docker.dao.DockerExecutorConfigDao;
import com.ezone.devops.plugins.job.other.docker.model.DockerExecutorConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class DockerExecutorConfigImpl extends BaseCommonDao<DockerExecutorConfig> implements DockerExecutorConfigDao {

}
