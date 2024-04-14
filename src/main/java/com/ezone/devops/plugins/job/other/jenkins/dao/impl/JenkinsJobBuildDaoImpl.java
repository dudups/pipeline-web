package com.ezone.devops.plugins.job.other.jenkins.dao.impl;

import com.ezone.devops.plugins.job.other.jenkins.dao.JenkinsJobBuildDao;
import com.ezone.devops.plugins.job.other.jenkins.model.JenkinsJobBuild;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class JenkinsJobBuildDaoImpl extends BaseCommonDao<JenkinsJobBuild> implements JenkinsJobBuildDao {
}
