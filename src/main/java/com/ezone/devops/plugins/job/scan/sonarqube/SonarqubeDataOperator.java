package com.ezone.devops.plugins.job.scan.sonarqube;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.exception.InitialJobException;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PipelineInitContext;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.scan.sonarqube.model.SonarqubeBuild;
import com.ezone.devops.plugins.job.scan.sonarqube.model.SonarqubeConfig;
import com.ezone.devops.plugins.job.scan.sonarqube.service.SonarqubeBuildService;
import com.ezone.devops.plugins.job.scan.sonarqube.service.SonarqubeConfigService;
import com.ezone.galaxy.framework.common.bean.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SonarqubeDataOperator implements PluginDataOperator<SonarqubeConfig, SonarqubeBuild> {

    @Autowired
    private SonarqubeBuildService sonarqubeBuildService;
    @Autowired
    private SonarqubeConfigService sonarqubeConfigService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        SonarqubeConfig sonarqubeConfig = json.toJavaObject(SonarqubeConfig.class);
        if (sonarqubeConfig == null) {
            return false;
        }
        if (sonarqubeConfig.getBuildImageId() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建镜像不允许为空");
        }
        if (StringUtils.isBlank(sonarqubeConfig.getProviderName())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的服务集成的名称不允许为空");
        }

        return true;
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        SonarqubeConfig sonarqubeConfig = json.toJavaObject(SonarqubeConfig.class);
        sonarqubeConfig = sonarqubeConfigService.saveConfig(sonarqubeConfig);
        return sonarqubeConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return sonarqubeConfigService.deleteConfig(id);
    }

    @Override
    public boolean updateRealJobRecord(Long realJobRecordId, JSONObject json) {
        SonarqubeBuild sonarqubeBuild = json.toJavaObject(SonarqubeBuild.class);
        return sonarqubeBuildService.updateBuild(sonarqubeBuild);
    }

    @Override
    public SonarqubeConfig getRealJob(Long id) {
        return sonarqubeConfigService.getById(id);
    }

    @Override
    public SonarqubeBuild getRealJobRecord(Long id) {
        return sonarqubeBuildService.getById(id);
    }

    @Override
    public Long initRealJobRecordByRealJobId(Long realJobConfigId, PipelineInitContext context) {
        SonarqubeConfig sonarqubeConfig = getRealJob(realJobConfigId);
        if (null == sonarqubeConfig) {
            log.error("init job error, config not exist,realJobConfigId:[{}]", realJobConfigId);
            throw new InitialJobException();
        }

        SonarqubeBuild sonarqubeBuild = new SonarqubeBuild();
        sonarqubeBuild = sonarqubeBuildService.saveBuild(sonarqubeBuild);
        log.debug("save build:[{}]", sonarqubeBuild);
        return sonarqubeBuild.getId();
    }
}
