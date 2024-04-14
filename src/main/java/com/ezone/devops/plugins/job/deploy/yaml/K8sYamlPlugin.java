package com.ezone.devops.plugins.job.deploy.yaml;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class K8sYamlPlugin implements Plugin {

    @Autowired
    private K8sYamlOperator k8sYamlOperator;
    @Autowired
    private K8sYamlDataOperator k8sYamlDataOperator;
    @Autowired
    private K8sYamlPluginInfo k8sYamlPluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return k8sYamlOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return k8sYamlDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(k8sYamlPluginInfo);
    }
}
