package com.ezone.devops.plugins.job.deploy.host.dao.impl;

import com.ezone.devops.plugins.job.deploy.host.dao.HostDeployConfigDao;
import com.ezone.devops.plugins.job.deploy.host.model.HostDeployConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class HostDeployConfigDaoImpl extends BaseCommonDao<HostDeployConfig> implements HostDeployConfigDao {

}
