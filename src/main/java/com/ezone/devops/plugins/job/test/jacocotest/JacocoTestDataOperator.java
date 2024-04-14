package com.ezone.devops.plugins.job.test.jacocotest;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.exception.InitialJobException;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PipelineInitContext;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.test.jacocotest.model.JacocoTestBuild;
import com.ezone.devops.plugins.job.test.jacocotest.model.JacocoTestConfig;
import com.ezone.devops.plugins.job.test.jacocotest.service.JacocoTestBuildService;
import com.ezone.devops.plugins.job.test.jacocotest.service.JacocoTestConfigService;
import com.ezone.galaxy.framework.common.bean.BaseException;
import com.ezone.galaxy.framework.common.config.I18nContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JacocoTestDataOperator implements PluginDataOperator<JacocoTestConfig, JacocoTestBuild> {

    @Autowired
    private JacocoTestBuildService jacocoTestBuildService;
    @Autowired
    private JacocoTestConfigService jacocoTestConfigService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        JacocoTestConfig jacocoTestConfig = json.toJavaObject(JacocoTestConfig.class);
        if (jacocoTestConfig == null) {
            return false;
        }
        if (jacocoTestConfig.getBuildImageId() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建镜像不允许为空");
        }

        if (jacocoTestConfig.getCloneMode() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的克隆策略不允许为空");
        }

        if (StringUtils.isBlank(jacocoTestConfig.getCommand())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的命令不能为空");
        }

        if (StringUtils.isBlank(jacocoTestConfig.getReportDir())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的报告目录不允许为空");
        }

        return true;
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        JacocoTestConfig jacocoTestConfig = json.toJavaObject(JacocoTestConfig.class);
        jacocoTestConfig = jacocoTestConfigService.saveConfig(jacocoTestConfig);
        return jacocoTestConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return jacocoTestConfigService.deleteConfig(id);
    }

    @Override
    public boolean updateRealJobRecord(Long realJobRecordId, JSONObject json) {
        JacocoTestBuild jacocoTestBuild = json.toJavaObject(JacocoTestBuild.class);
        return jacocoTestBuildService.updateBuild(jacocoTestBuild);
    }

    @Override
    public JacocoTestConfig getRealJob(Long id) {
        return jacocoTestConfigService.getById(id);
    }

    @Override
    public JacocoTestBuild getRealJobRecord(Long id) {
        return jacocoTestBuildService.getById(id);
    }

    @Override
    public Long initRealJobRecordByRealJobId(Long realJobConfigId, PipelineInitContext context) {
        JacocoTestConfig jacocoTestConfig = getRealJob(realJobConfigId);
        if (null == jacocoTestConfig) {
            log.error("init job error, config not exist,realJobConfigId:[{}]", realJobConfigId);
            throw new InitialJobException();
        }

        JacocoTestBuild jacocoTestBuild = new JacocoTestBuild();
        jacocoTestBuild = jacocoTestBuildService.saveBuild(jacocoTestBuild);
        log.debug("save build:[{}]", jacocoTestBuild);
        return jacocoTestBuild.getId();
    }
}
