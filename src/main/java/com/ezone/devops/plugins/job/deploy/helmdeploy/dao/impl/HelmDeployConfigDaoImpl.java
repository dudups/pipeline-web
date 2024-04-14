package com.ezone.devops.plugins.job.deploy.helmdeploy.dao.impl;

import com.ezone.devops.plugins.job.deploy.helmdeploy.dao.HelmDeployConfigDao;
import com.ezone.devops.plugins.job.deploy.helmdeploy.model.HelmDeployConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class HelmDeployConfigDaoImpl extends BaseCommonDao<HelmDeployConfig> implements HelmDeployConfigDao {

}
