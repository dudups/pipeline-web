package com.ezone.devops.plugins.job.build.gradle.docker.dao.impl;

import com.ezone.devops.plugins.job.build.gradle.docker.dao.GradleDockerConfigDao;
import com.ezone.devops.plugins.job.build.gradle.docker.model.GradleDockerConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class GradleDockerConfigDaoImpl extends BaseCommonDao<GradleDockerConfig> implements GradleDockerConfigDao {

}
