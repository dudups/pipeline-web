package com.ezone.devops.plugins.job.build.helm.dao;

import com.ezone.devops.plugins.job.build.helm.model.HelmPackageBuild;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface HelmPackageBuildDao extends LongKeyBaseDao<HelmPackageBuild> {

    String ID = "id";
}
