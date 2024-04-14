package com.ezone.devops.plugins.job.deploy.helmdeploy;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PipelineInitContext;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.deploy.helmdeploy.bean.HelmDeployBuildBean;
import com.ezone.devops.plugins.job.deploy.helmdeploy.bean.HelmDeployConfigBean;
import com.ezone.devops.plugins.job.deploy.helmdeploy.model.HelmDeployBuild;
import com.ezone.devops.plugins.job.deploy.helmdeploy.model.HelmDeployConfig;
import com.ezone.devops.plugins.job.deploy.helmdeploy.service.HelmDeployBuildService;
import com.ezone.devops.plugins.job.deploy.helmdeploy.service.HelmDeployConfigService;
import com.ezone.galaxy.framework.common.bean.BaseException;
import com.ezone.galaxy.framework.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HelmDeployDataOperator implements PluginDataOperator<HelmDeployConfigBean, HelmDeployBuildBean> {

    @Autowired
    private HelmDeployConfigService helmDeployConfigService;
    @Autowired
    private HelmDeployBuildService helmDeployBuildService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        HelmDeployConfigBean helmDeployConfigBean = json.toJavaObject(HelmDeployConfigBean.class);
        if (StringUtils.isBlank(helmDeployConfigBean.getClusterKey())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "集群的英文标识不能为空");
        }

        if (StringUtils.isBlank(helmDeployConfigBean.getNamespace())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "命名空间不能为空");
        }

        if (helmDeployConfigBean.getRepoType() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "制品库类型不能为空");
        }

        if (StringUtils.isBlank(helmDeployConfigBean.getChartName())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "chart名称不能为空");
        }

        if (helmDeployConfigBean.getVersionType() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "制品的版本类型不能为空");
        }

        if (StringUtils.isBlank(helmDeployConfigBean.getReleaseName())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "实例名称不能为空");
        }

        return true;
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        HelmDeployConfigBean helmDeployConfigBean = json.toJavaObject(HelmDeployConfigBean.class);
        HelmDeployConfig helmDeployConfig = new HelmDeployConfig();
        helmDeployConfig.setDataJson(JsonUtils.toJson(helmDeployConfigBean));
        helmDeployConfigService.saveConfig(helmDeployConfig);
        return helmDeployConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return helmDeployConfigService.deleteConfig(id);
    }

    @Override
    public HelmDeployConfigBean getRealJob(Long id) {
        HelmDeployConfig helmDeployConfig = helmDeployConfigService.findById(id);
        return JsonUtils.toObject(helmDeployConfig.getDataJson(), HelmDeployConfigBean.class);
    }

    @Override
    public boolean updateRealJobRecord(Long realJobRecordId, JSONObject json) {
        HelmDeployBuildBean helmDeployBuildBean = json.toJavaObject(HelmDeployBuildBean.class);
        HelmDeployBuild helmDeployBuild = new HelmDeployBuild();
        helmDeployBuild.setId(realJobRecordId);
        helmDeployBuild.setDataJson(JsonUtils.toJson(helmDeployBuildBean));
        return helmDeployBuildService.updateBuild(helmDeployBuild);
    }

    @Override
    public HelmDeployBuildBean getRealJobRecord(Long id) {
        HelmDeployBuild helmDeployBuild = helmDeployBuildService.getById(id);
        return JsonUtils.toObject(helmDeployBuild.getDataJson(), HelmDeployBuildBean.class);
    }

    @Override
    public Long initRealJobRecordByRealJobId(Long realJobConfigId, PipelineInitContext context) {
        HelmDeployBuild helmDeployBuild = new HelmDeployBuild();
        helmDeployBuild.setDataJson("{}");
        helmDeployBuildService.saveBuild(helmDeployBuild);
        return helmDeployBuild.getId();
    }
}
