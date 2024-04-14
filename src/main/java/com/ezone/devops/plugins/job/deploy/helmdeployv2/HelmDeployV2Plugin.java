package com.ezone.devops.plugins.job.deploy.helmdeployv2;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class HelmDeployV2Plugin implements Plugin {

    @Autowired
    private HelmDeployV2Operator helmDeployV2Operator;
    @Autowired
    private HelmDeployV2DataOperator helmDeployV2DataOperator;
    @Autowired
    private HelmDeployV2PluginInfo helmDeployV2PluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return helmDeployV2Operator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return helmDeployV2DataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(helmDeployV2PluginInfo);
    }
}
