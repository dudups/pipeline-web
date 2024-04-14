package com.ezone.devops.plugins.job.test.maventest;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.exception.InitialJobException;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PipelineInitContext;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.test.maventest.model.MavenTestBuild;
import com.ezone.devops.plugins.job.test.maventest.model.MavenTestConfig;
import com.ezone.devops.plugins.job.test.maventest.service.MavenTestBuildService;
import com.ezone.devops.plugins.job.test.maventest.service.MavenTestConfigService;
import com.ezone.galaxy.framework.common.bean.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MavenTestDataOperator implements PluginDataOperator<MavenTestConfig, MavenTestBuild> {

    @Autowired
    private MavenTestBuildService mavenTestBuildService;
    @Autowired
    private MavenTestConfigService mavenTestConfigService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        MavenTestConfig mavenTestConfig = json.toJavaObject(MavenTestConfig.class);
        if (mavenTestConfig == null) {
            return false;
        }
        if (mavenTestConfig.getBuildImageId() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的构建镜像不允许为空");
        }

        if (mavenTestConfig.getCloneMode() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的克隆策略不允许为空");
        }

        if (StringUtils.isBlank(mavenTestConfig.getCommand())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的命令不能为空");
        }

        if (StringUtils.isBlank(mavenTestConfig.getReportDir())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的报告目录不允许为空");
        }

        return true;
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        MavenTestConfig mavenTestConfig = json.toJavaObject(MavenTestConfig.class);
        mavenTestConfig = mavenTestConfigService.saveConfig(mavenTestConfig);
        return mavenTestConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return mavenTestConfigService.deleteConfig(id);
    }

    @Override
    public boolean updateRealJobRecord(Long realJobRecordId, JSONObject json) {
        MavenTestBuild mavenTestBuild = json.toJavaObject(MavenTestBuild.class);
        return mavenTestBuildService.updateBuild(mavenTestBuild);
    }

    @Override
    public MavenTestConfig getRealJob(Long id) {
        return mavenTestConfigService.getById(id);
    }

    @Override
    public MavenTestBuild getRealJobRecord(Long id) {
        return mavenTestBuildService.getById(id);
    }

    @Override
    public Long initRealJobRecordByRealJobId(Long realJobConfigId, PipelineInitContext context) {
        MavenTestConfig mavenTestConfig = getRealJob(realJobConfigId);
        if (null == mavenTestConfig) {
            log.error("init job error, config not exist,realJobConfigId:[{}]", realJobConfigId);
            throw new InitialJobException();
        }

        MavenTestBuild mavenTestBuild = new MavenTestBuild();
        mavenTestBuild = mavenTestBuildService.saveBuild(mavenTestBuild);
        log.debug("save build:[{}]", mavenTestBuild);
        return mavenTestBuild.getId();
    }
}
