package com.ezone.devops.plugins.job.build.helm.dao.impl;

import com.ezone.devops.plugins.job.build.helm.dao.HelmPackageConfigDao;
import com.ezone.devops.plugins.job.build.helm.model.HelmPackageConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class HelmPackageConfigImpl extends BaseCommonDao<HelmPackageConfig> implements HelmPackageConfigDao {

}
