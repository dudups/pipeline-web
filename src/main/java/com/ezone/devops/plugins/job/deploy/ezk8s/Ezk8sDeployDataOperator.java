package com.ezone.devops.plugins.job.deploy.ezk8s;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.exception.InitialJobException;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PipelineInitContext;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.deploy.ezk8s.bean.Ezk8sDeployConfigBean;
import com.ezone.devops.plugins.job.deploy.ezk8s.model.Ezk8sDeployBuild;
import com.ezone.devops.plugins.job.deploy.ezk8s.model.Ezk8sDeployConfig;
import com.ezone.devops.plugins.job.deploy.ezk8s.service.Ezk8sDeployBuildService;
import com.ezone.devops.plugins.job.deploy.ezk8s.service.Ezk8sDeployConfigService;
import com.ezone.galaxy.framework.common.bean.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Ezk8sDeployDataOperator implements PluginDataOperator<Ezk8sDeployConfigBean, Ezk8sDeployBuild> {

    @Autowired
    private Ezk8sDeployConfigService ezk8SDeployConfigService;
    @Autowired
    private Ezk8sDeployBuildService ezk8SDeployBuildService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        Ezk8sDeployConfigBean ezk8SDeployConfigBean = json.toJavaObject(Ezk8sDeployConfigBean.class);

        if (StringUtils.isBlank(ezk8SDeployConfigBean.getClusterName())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的k8s集群不能为空");
        }

        if (StringUtils.isBlank(ezk8SDeployConfigBean.getEnvName())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的k8s环境不能为空");
        }

        if (ezk8SDeployConfigBean.getDeployConfigId() == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的k8s模板环境不能为空");
        }

        if (StringUtils.isBlank(ezk8SDeployConfigBean.getDeployInstanceName())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的k8s实例不能为空");
        }

        return true;
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        Ezk8sDeployConfigBean ezk8SDeployConfigBean = json.toJavaObject(Ezk8sDeployConfigBean.class);
        Ezk8sDeployConfig ezk8SDeployConfig = new Ezk8sDeployConfig(ezk8SDeployConfigBean);
        ezk8SDeployConfigService.saveConfig(ezk8SDeployConfig);
        return ezk8SDeployConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return ezk8SDeployConfigService.deleteConfig(id);
    }

    @Override
    public boolean updateRealJobRecord(Long realJobRecordId, JSONObject json) {
        Ezk8sDeployBuild ezk8sDeployBuild = json.toJavaObject(Ezk8sDeployBuild.class);
        return ezk8SDeployBuildService.updateBuild(ezk8sDeployBuild);
    }

    @Override
    public Ezk8sDeployConfigBean getRealJob(Long id) {
        Ezk8sDeployConfig ezk8SDeployConfig = ezk8SDeployConfigService.findById(id);
        return new Ezk8sDeployConfigBean(ezk8SDeployConfig);
    }

    @Override
    public Ezk8sDeployBuild getRealJobRecord(Long id) {
        return ezk8SDeployBuildService.getById(id);
    }

    @Override
    public Long initRealJobRecordByRealJobId(Long realJobConfigId, PipelineInitContext context) {
        Ezk8sDeployConfigBean k8sDeployConfig = getRealJob(realJobConfigId);
        if (null == k8sDeployConfig) {
            log.error("cannot config, realJobConfId:[{}]", realJobConfigId);
            throw new InitialJobException();
        }

        Ezk8sDeployBuild ezk8SDeployBuild = new Ezk8sDeployBuild();
        ezk8SDeployBuildService.saveBuild(ezk8SDeployBuild);
        log.debug("save build:[{}]", ezk8SDeployBuild);
        return ezk8SDeployBuild.getId();
    }
}
