package com.ezone.devops.plugins.job.other.jenkins.service.impl;

import com.ezone.devops.plugins.job.other.jenkins.dao.JenkinsJobBuildDao;
import com.ezone.devops.plugins.job.other.jenkins.model.JenkinsJobBuild;
import com.ezone.devops.plugins.job.other.jenkins.service.JenkinsJobBuildService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JenkinsJobBuildServiceImpl implements JenkinsJobBuildService {

    @Autowired
    private JenkinsJobBuildDao jenkinsJobBuildDao;

    @Override
    public boolean updateJenkinsJobBuild(JenkinsJobBuild jenkinsJobBuild) {
        return jenkinsJobBuildDao.update(jenkinsJobBuild);
    }

    @Override
    public JenkinsJobBuild getById(Long id) {
        return jenkinsJobBuildDao.get(id);
    }

    @Override
    public JenkinsJobBuild saveJenkinsJobBuild(JenkinsJobBuild jenkinsJobBuild) {
        return jenkinsJobBuildDao.save(jenkinsJobBuild);
    }
}
