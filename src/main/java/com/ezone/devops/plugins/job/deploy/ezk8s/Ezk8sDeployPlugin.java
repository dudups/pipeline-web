package com.ezone.devops.plugins.job.deploy.ezk8s;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class Ezk8sDeployPlugin implements Plugin {

    @Autowired
    private Ezk8sDeployOperator ezk8SDeployOperator;
    @Autowired
    private Ezk8sDeployDataOperator ezk8SDeployDataOperator;
    @Autowired
    private Ezk8sDeployPluginInfo ezk8SDeployPluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return ezk8SDeployOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return ezk8SDeployDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(ezk8SDeployPluginInfo);
    }
}
