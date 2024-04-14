package com.ezone.devops.plugins.job.deploy.helmdeployv2;

import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.devops.plugins.job.PluginInfoAware;
import org.springframework.stereotype.Component;

@Component
public class HelmDeployV2PluginInfo implements PluginInfoAware {

    private static final String JOB_TYPE = "HELM-DEPLOY-V2";
    public static final String NAME = "Helm模板部署";
    private static final String DESCRIPTION = "向K8S管理模块中管理的集群使用Helm模板部署";

    @Override
    public PluginType getPluginType() {
        return PluginType.DEPLOY;
    }

    @Override
    public String getJobType() {
        return JOB_TYPE;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public boolean customDefinitionImage() {
        return false;
    }
}
