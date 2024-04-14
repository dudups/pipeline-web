package com.ezone.devops.plugins.job.deploy.helmdeployv2;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PipelineInitContext;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.deploy.helmdeployv2.bean.HelmDeployBuildV2Bean;
import com.ezone.devops.plugins.job.deploy.helmdeployv2.bean.HelmDeployConfigV2Bean;
import com.ezone.devops.plugins.job.deploy.helmdeployv2.model.HelmDeployV2Build;
import com.ezone.devops.plugins.job.deploy.helmdeployv2.model.HelmDeployV2Config;
import com.ezone.devops.plugins.job.deploy.helmdeployv2.service.HelmDeployV2BuildService;
import com.ezone.devops.plugins.job.deploy.helmdeployv2.service.HelmDeployV2ConfigService;
import com.ezone.galaxy.framework.common.bean.BaseException;
import com.ezone.galaxy.framework.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HelmDeployV2DataOperator implements PluginDataOperator<HelmDeployConfigV2Bean, HelmDeployBuildV2Bean> {

    @Autowired
    private HelmDeployV2ConfigService helmDeployV2ConfigService;
    @Autowired
    private HelmDeployV2BuildService helmDeployV2BuildService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        HelmDeployConfigV2Bean helmDeployConfigV2Bean = json.toJavaObject(HelmDeployConfigV2Bean.class);
        if (StringUtils.isBlank(helmDeployConfigV2Bean.getClusterKey())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "集群的英文标识不能为空");
        }

        if (StringUtils.isBlank(helmDeployConfigV2Bean.getNamespace())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "命名空间不能为空");
        }

        if (helmDeployConfigV2Bean.getTemplateId() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "制品库类型不能为空");
        }

        return true;
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        HelmDeployConfigV2Bean helmDeployConfigV2Bean = json.toJavaObject(HelmDeployConfigV2Bean.class);
        HelmDeployV2Config helmDeployV2Config = new HelmDeployV2Config();
        helmDeployV2Config.setDataJson(JsonUtils.toJson(helmDeployConfigV2Bean));
        helmDeployV2ConfigService.saveConfig(helmDeployV2Config);
        return helmDeployV2Config.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return helmDeployV2ConfigService.deleteConfig(id);
    }

    @Override
    public boolean updateRealJobRecord(Long realJobRecordId, JSONObject json) {
        HelmDeployBuildV2Bean helmDeployBuildV2Bean = json.toJavaObject(HelmDeployBuildV2Bean.class);
        HelmDeployV2Build helmDeployV2Build = new HelmDeployV2Build();
        helmDeployV2Build.setId(realJobRecordId);
        helmDeployV2Build.setDataJson(JsonUtils.toJson(helmDeployBuildV2Bean));
        return helmDeployV2BuildService.updateBuild(helmDeployV2Build);
    }

    @Override
    public HelmDeployConfigV2Bean getRealJob(Long id) {
        HelmDeployV2Config helmDeployV2Config = helmDeployV2ConfigService.findById(id);
        return JsonUtils.toObject(helmDeployV2Config.getDataJson(), HelmDeployConfigV2Bean.class);
    }

    @Override
    public HelmDeployBuildV2Bean getRealJobRecord(Long id) {
        HelmDeployV2Build helmDeployV2Build = helmDeployV2BuildService.getById(id);
        return JsonUtils.toObject(helmDeployV2Build.getDataJson(), HelmDeployBuildV2Bean.class);
    }

    @Override
    public Long initRealJobRecordByRealJobId(Long realJobConfigId, PipelineInitContext context) {
        HelmDeployV2Build helmDeployV2Build = new HelmDeployV2Build();
        helmDeployV2Build.setDataJson("{}");
        helmDeployV2Build = helmDeployV2BuildService.saveBuild(helmDeployV2Build);
        return helmDeployV2Build.getId();
    }
}
