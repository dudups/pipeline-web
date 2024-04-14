package com.ezone.devops.plugins.job.deploy.yaml;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PipelineInitContext;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.deploy.yaml.bean.K8sYamlBuildBean;
import com.ezone.devops.plugins.job.deploy.yaml.bean.K8sYamlConfigBean;
import com.ezone.devops.plugins.job.deploy.yaml.model.K8sYamlBuild;
import com.ezone.devops.plugins.job.deploy.yaml.model.K8sYamlConfig;
import com.ezone.devops.plugins.job.deploy.yaml.service.K8sYamlBuildService;
import com.ezone.devops.plugins.job.deploy.yaml.service.K8sYamlConfigService;
import com.ezone.galaxy.framework.common.bean.BaseException;
import com.ezone.galaxy.framework.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class K8sYamlDataOperator implements PluginDataOperator<K8sYamlConfigBean, K8sYamlBuildBean> {

    @Autowired
    private K8sYamlBuildService k8sYamlBuildService;
    @Autowired
    private K8sYamlConfigService k8sYamlConfigService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        K8sYamlConfigBean k8SYamlConfigBean = json.toJavaObject(K8sYamlConfigBean.class);
        if (StringUtils.isBlank(k8SYamlConfigBean.getClusterKey())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "集群的英文标识不能为空");
        }

        if (k8SYamlConfigBean.isUseRepoFile()) {
            if (StringUtils.isBlank(k8SYamlConfigBean.getRepoFilePath())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "yaml文件路径不能为空");
            }
        } else {
            if (StringUtils.isBlank(k8SYamlConfigBean.getYaml())) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "yaml内容不能为空");
            }
        }

        return true;
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        K8sYamlConfigBean k8sYamlConfigBean = json.toJavaObject(K8sYamlConfigBean.class);
        K8sYamlConfig yamlConfig = new K8sYamlConfig();
        yamlConfig.setDataJson(JsonUtils.toJson(k8sYamlConfigBean));
        k8sYamlConfigService.saveConfig(yamlConfig);
        return yamlConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return k8sYamlConfigService.deleteConfig(id);
    }


    @Override
    public K8sYamlConfigBean getRealJob(Long id) {
        K8sYamlConfig k8sYamlConfig = k8sYamlConfigService.findById(id);
        return JsonUtils.toObject(k8sYamlConfig.getDataJson(), K8sYamlConfigBean.class);
    }

    @Override
    public K8sYamlBuildBean getRealJobRecord(Long id) {
        K8sYamlBuild k8sYamlBuild = k8sYamlBuildService.findById(id);
        return JsonUtils.toObject(k8sYamlBuild.getDataJson(), K8sYamlBuildBean.class);
    }

    @Override
    public Long initRealJobRecordByRealJobId(Long realJobConfigId, PipelineInitContext context) {
        K8sYamlConfigBean k8sYamlConfigBean = getRealJob(realJobConfigId);
        K8sYamlBuildBean k8sYamlBuildBean = new K8sYamlBuildBean();
        k8sYamlBuildBean.setClusterKey(k8sYamlConfigBean.getClusterKey());
        K8sYamlBuild k8sYamlBuild = new K8sYamlBuild();
        k8sYamlBuild.setDataJson(JsonUtils.toJson(k8sYamlBuildBean));
        k8sYamlBuild = k8sYamlBuildService.saveBuild(k8sYamlBuild);
        return k8sYamlBuild.getId();
    }
}
