package com.ezone.devops.plugins.job.build.gradle.file.dao.impl;

import com.ezone.devops.plugins.job.build.gradle.file.dao.GradleBuildDao;
import com.ezone.devops.plugins.job.build.gradle.file.model.GradleBuild;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class GradleBuildDaoImpl extends BaseCommonDao<GradleBuild> implements GradleBuildDao {

}
