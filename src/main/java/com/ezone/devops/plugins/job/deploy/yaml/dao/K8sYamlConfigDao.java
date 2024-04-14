package com.ezone.devops.plugins.job.deploy.yaml.dao;

import com.ezone.devops.plugins.job.deploy.yaml.model.K8sYamlConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface K8sYamlConfigDao extends LongKeyBaseDao<K8sYamlConfig> {

    String ID = "id";
    String DATA_JSON = "data_json";

}
