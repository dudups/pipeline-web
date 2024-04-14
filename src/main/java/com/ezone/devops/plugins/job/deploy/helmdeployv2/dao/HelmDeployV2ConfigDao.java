package com.ezone.devops.plugins.job.deploy.helmdeployv2.dao;

import com.ezone.devops.plugins.job.deploy.helmdeployv2.model.HelmDeployV2Config;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface HelmDeployV2ConfigDao extends LongKeyBaseDao<HelmDeployV2Config> {

    String ID = "id";
    String DATA_JSON = "data_json";

}
