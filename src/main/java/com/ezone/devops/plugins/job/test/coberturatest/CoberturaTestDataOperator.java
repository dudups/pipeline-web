package com.ezone.devops.plugins.job.test.coberturatest;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.exception.InitialJobException;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PipelineInitContext;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.test.coberturatest.model.CoberturaTestBuild;
import com.ezone.devops.plugins.job.test.coberturatest.model.CoberturaTestConfig;
import com.ezone.devops.plugins.job.test.coberturatest.service.CoberturaTestBuildService;
import com.ezone.devops.plugins.job.test.coberturatest.service.CoberturaTestConfigService;
import com.ezone.galaxy.framework.common.bean.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CoberturaTestDataOperator implements PluginDataOperator<CoberturaTestConfig, CoberturaTestBuild> {

    @Autowired
    private CoberturaTestBuildService coberturaTestBuildService;
    @Autowired
    private CoberturaTestConfigService coberturaTestConfigService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        CoberturaTestConfig coberturaTestConfig = json.toJavaObject(CoberturaTestConfig.class);
        if (coberturaTestConfig == null) {
            return false;
        }
        if (coberturaTestConfig.getBuildImageId() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建镜像不允许为空");
        }

        if (coberturaTestConfig.getCloneMode() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的克隆策略不允许为空");
        }

        if (StringUtils.isBlank(coberturaTestConfig.getCommand())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的命令不能为空");
        }

        if (StringUtils.isBlank(coberturaTestConfig.getReportDir())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的报告目录不允许为空");
        }

        return true;
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        CoberturaTestConfig coberturaTestConfig = json.toJavaObject(CoberturaTestConfig.class);
        coberturaTestConfig = coberturaTestConfigService.saveConfig(coberturaTestConfig);
        return coberturaTestConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return coberturaTestConfigService.deleteConfig(id);
    }

    @Override
    public boolean updateRealJobRecord(Long realJobRecordId, JSONObject json) {
        CoberturaTestBuild coberturaTestBuild = json.toJavaObject(CoberturaTestBuild.class);
        return coberturaTestBuildService.updateBuild(coberturaTestBuild);
    }

    @Override
    public CoberturaTestConfig getRealJob(Long id) {
        return coberturaTestConfigService.getById(id);
    }

    @Override
    public CoberturaTestBuild getRealJobRecord(Long id) {
        return coberturaTestBuildService.getById(id);
    }

    @Override
    public Long initRealJobRecordByRealJobId(Long realJobConfigId, PipelineInitContext context) {
        CoberturaTestConfig coberturaTestConfig = getRealJob(realJobConfigId);
        if (null == coberturaTestConfig) {
            log.error("init job error, config not exist,realJobConfigId:[{}]", realJobConfigId);
            throw new InitialJobException();
        }

        CoberturaTestBuild coberturaTestBuild = new CoberturaTestBuild();
        coberturaTestBuild = coberturaTestBuildService.saveBuild(coberturaTestBuild);
        log.debug("save build:[{}]", coberturaTestBuild);
        return coberturaTestBuild.getId();
    }
}
