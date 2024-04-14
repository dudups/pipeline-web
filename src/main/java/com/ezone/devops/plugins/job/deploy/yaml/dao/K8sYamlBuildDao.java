package com.ezone.devops.plugins.job.deploy.yaml.dao;

import com.ezone.devops.plugins.job.deploy.yaml.model.K8sYamlBuild;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface K8sYamlBuildDao extends LongKeyBaseDao<K8sYamlBuild> {

    String ID = "id";
    String DATA_JSON = "data_json";

}
