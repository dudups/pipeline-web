package com.ezone.devops.plugins.job.deploy.helmdeployv2.dao;

import com.ezone.devops.plugins.job.deploy.helmdeployv2.model.HelmDeployV2Build;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface HelmDeployV2BuildDao extends LongKeyBaseDao<HelmDeployV2Build> {

    String ID = "id";
    String DATA_JSON = "data_json";

}
