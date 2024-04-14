package com.ezone.devops.plugins.job.test.eztest.performance;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PipelineInitContext;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.test.eztest.performance.bean.EzTestPerformanceBuildBean;
import com.ezone.devops.plugins.job.test.eztest.performance.bean.EzTestPerformanceConfigBean;
import com.ezone.devops.plugins.job.test.eztest.performance.model.EzTestPerformanceBuild;
import com.ezone.devops.plugins.job.test.eztest.performance.model.EzTestPerformanceConfig;
import com.ezone.devops.plugins.job.test.eztest.performance.service.EzTestPerformanceBuildService;
import com.ezone.devops.plugins.job.test.eztest.performance.service.EzTestPerformanceConfigService;
import com.ezone.galaxy.framework.common.bean.BaseException;
import com.ezone.galaxy.framework.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EzTestPerformanceDataOperator implements PluginDataOperator<EzTestPerformanceConfigBean, EzTestPerformanceBuildBean> {

    @Autowired
    private EzTestPerformanceBuildService ezTestPerformanceBuildService;
    @Autowired
    private EzTestPerformanceConfigService ezTestPerformanceConfigService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        EzTestPerformanceConfigBean ezTestPerformanceConfig = json.toJavaObject(EzTestPerformanceConfigBean.class);
        if (ezTestPerformanceConfig == null) {
            return false;
        }
        if (ezTestPerformanceConfig.getSpaceId() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的测试空间不允许为空");
        }

        if (ezTestPerformanceConfig.getSuiteId() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的测试套件不允许为空");
        }

        if (ezTestPerformanceConfig.isUseCustomCluster()) {
            if (ezTestPerformanceConfig.getResourceType() == null) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的资源类型不允许为空");
            }

            if (StringUtils.isBlank(ezTestPerformanceConfig.getResourceName())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的资源名称不允许为空");
            }
        }
        return true;
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        EzTestPerformanceConfigBean ezTestPerformanceConfigBean = json.toJavaObject(EzTestPerformanceConfigBean.class);
        EzTestPerformanceConfig ezTestPerformanceConfig = new EzTestPerformanceConfig();
        ezTestPerformanceConfig.setDataJson(JsonUtils.toJson(ezTestPerformanceConfigBean));
        ezTestPerformanceConfig = ezTestPerformanceConfigService.saveConfig(ezTestPerformanceConfig);
        return ezTestPerformanceConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return ezTestPerformanceConfigService.deleteConfig(id);
    }

    @Override
    public boolean updateRealJobRecord(Long realJobRecordId, JSONObject json) {
        EzTestPerformanceBuildBean ezTestPerformanceBuildBean = json.toJavaObject(EzTestPerformanceBuildBean.class);
        EzTestPerformanceBuild ezTestPerformanceBuild = new EzTestPerformanceBuild();
        ezTestPerformanceBuild.setId(realJobRecordId);
        ezTestPerformanceBuild.setDataJson(JsonUtils.toJson(ezTestPerformanceBuildBean));
        return ezTestPerformanceBuildService.updateBuild(ezTestPerformanceBuild);
    }

    @Override
    public EzTestPerformanceConfigBean getRealJob(Long id) {
        EzTestPerformanceConfig ezTestPerformanceConfig = ezTestPerformanceConfigService.getById(id);
        if (ezTestPerformanceConfig == null) {
            return null;
        }
        return JsonUtils.toObject(ezTestPerformanceConfig.getDataJson(), EzTestPerformanceConfigBean.class);
    }

    @Override
    public EzTestPerformanceBuildBean getRealJobRecord(Long id) {
        EzTestPerformanceBuild testPerformanceBuild = ezTestPerformanceBuildService.getById(id);
        if (testPerformanceBuild == null) {
            return null;
        }
        return JsonUtils.toObject(testPerformanceBuild.getDataJson(), EzTestPerformanceBuildBean.class);
    }

    @Override
    public Long initRealJobRecordByRealJobId(Long realJobConfigId, PipelineInitContext context) {
        EzTestPerformanceBuild ezTestPerformanceBuild = new EzTestPerformanceBuild();
        ezTestPerformanceBuild.setDataJson("{}");
        ezTestPerformanceBuild = ezTestPerformanceBuildService.saveBuild(ezTestPerformanceBuild);
        return ezTestPerformanceBuild.getId();
    }
}
