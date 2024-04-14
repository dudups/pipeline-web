package com.ezone.devops.plugins.job.test.eztest.api;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.exception.InitialJobException;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PipelineInitContext;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.test.eztest.api.model.EzTestBuild;
import com.ezone.devops.plugins.job.test.eztest.api.model.EzTestConfig;
import com.ezone.devops.plugins.job.test.eztest.api.service.EzTestBuildService;
import com.ezone.devops.plugins.job.test.eztest.api.service.EzTestConfigService;
import com.ezone.galaxy.framework.common.bean.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EzTestDataOperator implements PluginDataOperator<EzTestConfig, EzTestBuild> {

    @Autowired
    private EzTestBuildService ezTestBuildService;
    @Autowired
    private EzTestConfigService ezTestConfigService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        EzTestConfig ezTestConfig = json.toJavaObject(EzTestConfig.class);
        if (ezTestConfig == null) {
            return false;
        }
        if (ezTestConfig.getPlConfId() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的测试空间不允许为空");
        }

        if (ezTestConfig.getSuiteId() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的测试套件不允许为空");
        }

        return true;
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        EzTestConfig ezTestConfig = json.toJavaObject(EzTestConfig.class);
        ezTestConfig = ezTestConfigService.saveConfig(ezTestConfig);
        return ezTestConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return ezTestConfigService.deleteConfig(id);
    }

    @Override
    public boolean updateRealJobRecord(Long realJobRecordId, JSONObject json) {
        EzTestBuild ezTestBuild = json.toJavaObject(EzTestBuild.class);
        return ezTestBuildService.updateBuild(ezTestBuild);
    }

    @Override
    public EzTestConfig getRealJob(Long id) {
        return ezTestConfigService.getById(id);
    }

    @Override
    public EzTestBuild getRealJobRecord(Long id) {
        return ezTestBuildService.getById(id);
    }

    @Override
    public Long initRealJobRecordByRealJobId(Long realJobConfigId, PipelineInitContext context) {
        EzTestConfig ezTestConfig = getRealJob(realJobConfigId);
        if (null == ezTestConfig) {
            log.error("init job error, config not exist,realJobConfigId:[{}]", realJobConfigId);
            throw new InitialJobException();
        }

        EzTestBuild ezTestBuild = new EzTestBuild();
        ezTestBuild = ezTestBuildService.saveBuild(ezTestBuild);
        log.debug("save build:[{}]", ezTestBuild);
        return ezTestBuild.getId();
    }
}
