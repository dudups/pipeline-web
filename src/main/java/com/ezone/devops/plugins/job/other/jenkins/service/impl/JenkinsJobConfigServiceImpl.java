package com.ezone.devops.plugins.job.other.jenkins.service.impl;

import com.ezone.devops.plugins.job.other.jenkins.dao.JenkinsJobConfigDao;
import com.ezone.devops.plugins.job.other.jenkins.model.JenkinsJobConfig;
import com.ezone.devops.plugins.job.other.jenkins.service.JenkinsJobConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JenkinsJobConfigServiceImpl implements JenkinsJobConfigService {

    @Autowired
    private JenkinsJobConfigDao jenkinsJobConfigDao;

    @Override
    public boolean deleteJenkinsJobConfig(Long id) {
        return jenkinsJobConfigDao.delete(id);
    }

    @Override
    public JenkinsJobConfig getById(Long id) {
        return jenkinsJobConfigDao.get(id);
    }

    @Override
    public JenkinsJobConfig saveJenkinsJobConfig(JenkinsJobConfig jenkinsJobConfig) {
        return jenkinsJobConfigDao.save(jenkinsJobConfig);
    }
}
