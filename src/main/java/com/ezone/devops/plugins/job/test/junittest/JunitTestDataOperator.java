package com.ezone.devops.plugins.job.test.junittest;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.exception.InitialJobException;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PipelineInitContext;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.test.junittest.model.JunitTestBuild;
import com.ezone.devops.plugins.job.test.junittest.model.JunitTestConfig;
import com.ezone.devops.plugins.job.test.junittest.service.JunitTestBuildService;
import com.ezone.devops.plugins.job.test.junittest.service.JunitTestConfigService;
import com.ezone.galaxy.framework.common.bean.BaseException;
import com.ezone.galaxy.framework.common.config.I18nContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JunitTestDataOperator implements PluginDataOperator<JunitTestConfig, JunitTestBuild> {

    @Autowired
    private JunitTestBuildService junitTestBuildService;
    @Autowired
    private JunitTestConfigService junitTestConfigService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        JunitTestConfig junitTestConfig = json.toJavaObject(JunitTestConfig.class);
        if (junitTestConfig == null) {
            return false;
        }
        if (junitTestConfig.getBuildImageId() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建镜像不允许为空");
        }

        if (junitTestConfig.getCloneMode() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的克隆策略不允许为空");
        }

        if (StringUtils.isBlank(junitTestConfig.getCommand())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的命令不能为空");
        }

        if (StringUtils.isBlank(junitTestConfig.getReportDir())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的报告目录不允许为空");
        }

        return true;
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        JunitTestConfig junitTestConfig = json.toJavaObject(JunitTestConfig.class);
        junitTestConfig = junitTestConfigService.saveConfig(junitTestConfig);
        return junitTestConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return junitTestConfigService.deleteConfig(id);
    }

    @Override
    public boolean updateRealJobRecord(Long realJobRecordId, JSONObject json) {
        JunitTestBuild junitTestBuild = json.toJavaObject(JunitTestBuild.class);
        return junitTestBuildService.updateBuild(junitTestBuild);
    }

    @Override
    public JunitTestConfig getRealJob(Long id) {
        return junitTestConfigService.getById(id);
    }

    @Override
    public JunitTestBuild getRealJobRecord(Long id) {
        return junitTestBuildService.getById(id);
    }

    @Override
    public Long initRealJobRecordByRealJobId(Long realJobConfigId, PipelineInitContext context) {
        JunitTestConfig junitTestConfig = getRealJob(realJobConfigId);
        if (null == junitTestConfig) {
            log.error("init job error, config not exist,realJobConfigId:[{}]", realJobConfigId);
            throw new InitialJobException();
        }

        JunitTestBuild junitTestBuild = new JunitTestBuild();
        junitTestBuild = junitTestBuildService.saveBuild(junitTestBuild);
        log.debug("save build:[{}]", junitTestBuild);
        return junitTestBuild.getId();
    }
}
