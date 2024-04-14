package com.ezone.devops.plugins.job.deploy.helmdeploy;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class HelmDeployPlugin implements Plugin {

    @Autowired
    private HelmDeployOperator helmDeployOperator;
    @Autowired
    private HelmDeployDataOperator helmDeployDataOperator;
    @Autowired
    private HelmDeployPluginInfo helmDeployPluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return helmDeployOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return helmDeployDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(helmDeployPluginInfo);
    }
}
