package com.ezone.devops.plugins.job.other.jenkins.dao.impl;

import com.ezone.devops.plugins.job.other.jenkins.dao.JenkinsJobConfigDao;
import com.ezone.devops.plugins.job.other.jenkins.model.JenkinsJobConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class JenkinsJobConfigDaoImpl extends BaseCommonDao<JenkinsJobConfig> implements JenkinsJobConfigDao {
}
