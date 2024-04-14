package com.ezone.devops.plugins.job.deploy.ezk8s.dao.impl;

import com.ezone.devops.plugins.job.deploy.ezk8s.dao.Ezk8sDeployConfigDao;
import com.ezone.devops.plugins.job.deploy.ezk8s.model.Ezk8sDeployConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class Ezk8SDeployConfigDaoImpl extends BaseCommonDao<Ezk8sDeployConfig> implements Ezk8sDeployConfigDao {

}
