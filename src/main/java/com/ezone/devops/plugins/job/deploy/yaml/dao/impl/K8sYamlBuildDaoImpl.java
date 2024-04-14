package com.ezone.devops.plugins.job.deploy.yaml.dao.impl;

import com.ezone.devops.plugins.job.deploy.yaml.dao.K8sYamlBuildDao;
import com.ezone.devops.plugins.job.deploy.yaml.model.K8sYamlBuild;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class K8sYamlBuildDaoImpl extends BaseCommonDao<K8sYamlBuild> implements K8sYamlBuildDao {

}
