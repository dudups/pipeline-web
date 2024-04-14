package com.ezone.devops.plugins.job.scan.ezscan;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PipelineInitContext;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.scan.ezscan.bean.EzScanBuildBean;
import com.ezone.devops.plugins.job.scan.ezscan.bean.EzScanConfigBean;
import com.ezone.devops.plugins.job.scan.ezscan.model.EzScanBuild;
import com.ezone.devops.plugins.job.scan.ezscan.model.EzScanConfig;
import com.ezone.devops.plugins.job.scan.ezscan.service.EzScanBuildService;
import com.ezone.devops.plugins.job.scan.ezscan.service.EzScanConfigService;
import com.ezone.galaxy.framework.common.bean.BaseException;
import com.ezone.galaxy.framework.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EzScanDataOperator implements PluginDataOperator<EzScanConfigBean, EzScanBuildBean> {

    @Autowired
    private EzScanBuildService ezScanBuildService;
    @Autowired
    private EzScanConfigService ezScanConfigService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        EzScanConfigBean ezScanConfigBean = json.toJavaObject(EzScanConfigBean.class);
        if (ezScanConfigBean.getScanLevel() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "扫描级别不能为空");
        }

        if (ezScanConfigBean.getRulesetId() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "规则集不能为空");
        }

        if (ezScanConfigBean.isEnableQos()) {
            if (ezScanConfigBean.getQosCount() < 0) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "问题数量必须是正整数");
            }

            if (ezScanConfigBean.getQosLevel() == null) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "扫描问题门禁级别不能为空");
            }
        }

        if (ezScanConfigBean.isUseSelfCiPool()) {
            if (StringUtils.isBlank(ezScanConfigBean.getClusterName())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "主机集群名称不能为空");
            }
        }

        return true;
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        EzScanConfigBean ezScanConfigBean = json.toJavaObject(EzScanConfigBean.class);
        EzScanConfig ezScanConfig = new EzScanConfig();
        ezScanConfig.setDataJson(JsonUtils.toJson(ezScanConfigBean));
        ezScanConfig = ezScanConfigService.saveConfig(ezScanConfig);
        return ezScanConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return ezScanConfigService.deleteConfig(id);
    }

    @Override
    public boolean updateRealJobRecord(Long realJobRecordId, JSONObject json) {
        EzScanBuildBean ezScanBuildBean = json.toJavaObject(EzScanBuildBean.class);
        EzScanBuild ezScanBuild = new EzScanBuild();
        ezScanBuild.setId(realJobRecordId);
        ezScanBuild.setDataJson(JsonUtils.toJson(ezScanBuildBean));
        return ezScanBuildService.updateBuild(ezScanBuild);
    }

    @Override
    public EzScanConfigBean getRealJob(Long id) {
        EzScanConfig ezScanConfig = ezScanConfigService.getById(id);
        return JsonUtils.toObject(ezScanConfig.getDataJson(), EzScanConfigBean.class);
    }

    @Override
    public EzScanBuildBean getRealJobRecord(Long id) {
        EzScanBuild ezScanBuild = ezScanBuildService.getById(id);
        return JsonUtils.toObject(ezScanBuild.getDataJson(), EzScanBuildBean.class);
    }

    @Override
    public Long initRealJobRecordByRealJobId(Long realJobConfigId, PipelineInitContext context) {
        EzScanBuild ezScanBuild = new EzScanBuild();
        ezScanBuild = ezScanBuildService.saveBuild(ezScanBuild);
        return ezScanBuild.getId();
    }
}
