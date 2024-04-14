package com.ezone.devops.plugins.job.deploy.helmdeploy.dao.impl;

import com.ezone.devops.plugins.job.deploy.helmdeploy.dao.HelmDeployBuildDao;
import com.ezone.devops.plugins.job.deploy.helmdeploy.model.HelmDeployBuild;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class HelmDeployBuildDaoImpl extends BaseCommonDao<HelmDeployBuild> implements HelmDeployBuildDao {

}
