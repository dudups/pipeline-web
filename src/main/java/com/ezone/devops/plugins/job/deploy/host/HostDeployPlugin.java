package com.ezone.devops.plugins.job.deploy.host;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class HostDeployPlugin implements Plugin {

    @Autowired
    private HostDeployOperator hostDeployOperator;
    @Autowired
    private HostDeployDataOperator hostDeployDataOperator;
    @Autowired
    private HostDeployPluginInfo hostDeployPluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return hostDeployOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return hostDeployDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(hostDeployPluginInfo);
    }
}
