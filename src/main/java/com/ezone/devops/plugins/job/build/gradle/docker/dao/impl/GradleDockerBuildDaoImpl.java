package com.ezone.devops.plugins.job.build.gradle.docker.dao.impl;

import com.ezone.devops.plugins.job.build.gradle.docker.dao.GradleDockerBuildDao;
import com.ezone.devops.plugins.job.build.gradle.docker.model.GradleDockerBuild;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class GradleDockerBuildDaoImpl extends BaseCommonDao<GradleDockerBuild> implements GradleDockerBuildDao {

}
