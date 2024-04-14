package com.ezone.devops.plugins.job.deploy.ezk8s.dao;

import com.ezone.devops.plugins.job.deploy.ezk8s.model.Ezk8sDeployConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface Ezk8sDeployConfigDao extends LongKeyBaseDao<Ezk8sDeployConfig> {

    String ID = "id";
    String CLUSTER_NAME = "cluster_name";
    String ENV_NAME = "env_name";
    String DEPLOY_INSTANCE_NAME = "deploy_instance_name";
    String DEPLOY_CONFIG_ID = "deploy_config_id";
    String REPLICAS = "replicas";
    String LABELS = "labels";
    String NODE_SELECTORS = "node_selectors";
    String VERSION_TYPE = "version_type";
    String CUSTOM_VERSION = "custom_version";

}
