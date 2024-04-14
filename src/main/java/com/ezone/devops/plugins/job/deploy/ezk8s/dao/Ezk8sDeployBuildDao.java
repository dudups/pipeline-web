package com.ezone.devops.plugins.job.deploy.ezk8s.dao;

import com.ezone.devops.plugins.job.deploy.ezk8s.model.Ezk8sDeployBuild;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface Ezk8sDeployBuildDao extends LongKeyBaseDao<Ezk8sDeployBuild> {

    String ID = "id";
    String DESCRIPTION = "description";
    String DASHBOARD_URL = "dashboard_url";

}
