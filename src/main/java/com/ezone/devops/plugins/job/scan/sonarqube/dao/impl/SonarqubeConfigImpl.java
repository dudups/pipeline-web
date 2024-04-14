package com.ezone.devops.plugins.job.scan.sonarqube.dao.impl;

import com.ezone.devops.plugins.job.scan.sonarqube.dao.SonarqubeConfigDao;
import com.ezone.devops.plugins.job.scan.sonarqube.model.SonarqubeConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class SonarqubeConfigImpl extends BaseCommonDao<SonarqubeConfig> implements SonarqubeConfigDao {

}
