package com.ezone.devops.plugins.job.build.helm.dao.impl;

import com.ezone.devops.plugins.job.build.helm.dao.HelmPackageBuildDao;
import com.ezone.devops.plugins.job.build.helm.model.HelmPackageBuild;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class HelmPackageBuildDaoImpl extends BaseCommonDao<HelmPackageBuild> implements HelmPackageBuildDao {

}
