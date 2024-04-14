package com.ezone.devops.plugins.job.deploy.yaml.dao.impl;

import com.ezone.devops.plugins.job.deploy.yaml.dao.K8sYamlConfigDao;
import com.ezone.devops.plugins.job.deploy.yaml.model.K8sYamlConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class K8sYamlConfigDaoImpl extends BaseCommonDao<K8sYamlConfig> implements K8sYamlConfigDao {

}
