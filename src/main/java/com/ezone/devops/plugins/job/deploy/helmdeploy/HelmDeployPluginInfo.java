package com.ezone.devops.plugins.job.deploy.helmdeploy;

import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.devops.plugins.job.PluginInfoAware;
import org.springframework.stereotype.Component;

@Component
public class HelmDeployPluginInfo implements PluginInfoAware {

    private static final String JOB_TYPE = "HELM-DEPLOY";
    public static final String NAME = "Helm部署";
    private static final String DESCRIPTION = "向K8S管理模块中管理的集群执行Helm部署";

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
