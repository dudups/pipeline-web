package com.ezone.devops.plugins.job.deploy.helmdeploy.dao;

import com.ezone.devops.plugins.job.deploy.helmdeploy.model.HelmDeployConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface HelmDeployConfigDao extends LongKeyBaseDao<HelmDeployConfig> {

    String ID = "id";
    String DATA_JSON = "data_json";

}
