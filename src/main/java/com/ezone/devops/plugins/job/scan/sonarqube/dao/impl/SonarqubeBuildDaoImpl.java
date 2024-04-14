package com.ezone.devops.plugins.job.scan.sonarqube.dao.impl;

import com.ezone.devops.plugins.job.scan.sonarqube.dao.SonarqubeBuildDao;
import com.ezone.devops.plugins.job.scan.sonarqube.model.SonarqubeBuild;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class SonarqubeBuildDaoImpl extends BaseCommonDao<SonarqubeBuild> implements SonarqubeBuildDao {

}
