package com.ezone.devops.plugins.job.build.gradle.file.dao.impl;

import com.ezone.devops.plugins.job.build.gradle.file.dao.GradleConfigDao;
import com.ezone.devops.plugins.job.build.gradle.file.model.GradleConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class GradleConfigDaoImpl extends BaseCommonDao<GradleConfig> implements GradleConfigDao {

}
