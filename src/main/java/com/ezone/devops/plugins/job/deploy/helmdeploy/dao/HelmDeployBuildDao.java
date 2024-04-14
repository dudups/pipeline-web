package com.ezone.devops.plugins.job.deploy.helmdeploy.dao;

import com.ezone.devops.plugins.job.deploy.helmdeploy.model.HelmDeployBuild;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface HelmDeployBuildDao extends LongKeyBaseDao<HelmDeployBuild> {

    String ID = "id";
    String DATA_JSON = "data_json";

}
