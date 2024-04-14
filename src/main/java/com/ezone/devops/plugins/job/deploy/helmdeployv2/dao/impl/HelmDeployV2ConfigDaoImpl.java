package com.ezone.devops.plugins.job.deploy.helmdeployv2.dao.impl;

import com.ezone.devops.plugins.job.deploy.helmdeployv2.dao.HelmDeployV2ConfigDao;
import com.ezone.devops.plugins.job.deploy.helmdeployv2.model.HelmDeployV2Config;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class HelmDeployV2ConfigDaoImpl extends BaseCommonDao<HelmDeployV2Config> implements HelmDeployV2ConfigDao {

}
