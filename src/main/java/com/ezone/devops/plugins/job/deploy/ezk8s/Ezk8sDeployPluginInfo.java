package com.ezone.devops.plugins.job.deploy.ezk8s;

import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.devops.plugins.job.PluginInfoAware;
import org.springframework.stereotype.Component;

@Component
public class Ezk8sDeployPluginInfo implements PluginInfoAware {

    private static final String JOB_TYPE = "EZK8S-DEPLOY";
    private static final String NAME = "K8S部署发单";
    private static final String DESCRIPTION = "创建K8S部署(Deprecated，建议改用K8S管理)模块中的部署单";

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
