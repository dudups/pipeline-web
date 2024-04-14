package com.ezone.devops.plugins.job.deploy.yaml;

import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.devops.plugins.job.PluginInfoAware;
import org.springframework.stereotype.Component;

@Component
public class K8sYamlPluginInfo implements PluginInfoAware {

    private static final String JOB_TYPE = "K8S-YAML-DEPLOY";
    public static final String NAME = "Yaml部署";
    private static final String DESCRIPTION = "使用Yaml文件直接向K8S管理模块中管理的集群发起部署";

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
