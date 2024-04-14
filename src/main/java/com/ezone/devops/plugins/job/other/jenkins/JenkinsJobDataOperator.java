package com.ezone.devops.plugins.job.other.jenkins;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.exception.InitialJobException;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PipelineInitContext;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.other.jenkins.model.JenkinsJobBuild;
import com.ezone.devops.plugins.job.other.jenkins.model.JenkinsJobConfig;
import com.ezone.devops.plugins.job.other.jenkins.service.JenkinsJobBuildService;
import com.ezone.devops.plugins.job.other.jenkins.service.JenkinsJobConfigService;
import com.ezone.galaxy.framework.common.bean.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JenkinsJobDataOperator implements PluginDataOperator<JenkinsJobConfig, JenkinsJobBuild> {

    @Autowired
    private JenkinsJobConfigService jenkinsJobConfigService;
    @Autowired
    private JenkinsJobBuildService jenkinsJobBuildService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        JenkinsJobConfig jenkinsJobConfig = json.toJavaObject(JenkinsJobConfig.class);
        if (StringUtils.isBlank(jenkinsJobConfig.getJenkinsName())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "jenkins名称不允许为空");
        }

        if (StringUtils.isBlank(jenkinsJobConfig.getJenkinsJobName())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "jenkins job名称不允许为空");
        }

        return true;
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        JenkinsJobConfig jenkinsJobConfig = json.toJavaObject(JenkinsJobConfig.class);
        jenkinsJobConfigService.saveJenkinsJobConfig(jenkinsJobConfig);
        return jenkinsJobConfig.getId();

    }

    @Override
    public boolean deleteRealJob(Long id) {
        return jenkinsJobConfigService.deleteJenkinsJobConfig(id);
    }

    @Override
    public boolean updateRealJobRecord(Long realJobRecordId, JSONObject json) {
        JenkinsJobBuild shellExecutorBuild = json.toJavaObject(JenkinsJobBuild.class);
        return jenkinsJobBuildService.updateJenkinsJobBuild(shellExecutorBuild);
    }

    @Override
    public JenkinsJobConfig getRealJob(Long id) {
        return jenkinsJobConfigService.getById(id);
    }

    @Override
    public JenkinsJobBuild getRealJobRecord(Long id) {
        return jenkinsJobBuildService.getById(id);
    }

    @Override
    public Long initRealJobRecordByRealJobId(Long realJobConfigId, PipelineInitContext context) {
        JenkinsJobConfig jenkinsJobConfig = getRealJob(realJobConfigId);
        if (null == jenkinsJobConfig) {
            log.error("can't init jenkins build info, conf id: {}", realJobConfigId);
            throw new InitialJobException();
        }

        JenkinsJobBuild jenkinsJobBuild = new JenkinsJobBuild();
        jenkinsJobBuildService.saveJenkinsJobBuild(jenkinsJobBuild);
        return jenkinsJobBuild.getId();
    }
}
