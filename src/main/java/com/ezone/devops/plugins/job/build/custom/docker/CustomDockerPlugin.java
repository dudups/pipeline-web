package com.ezone.devops.plugins.job.build.custom.docker;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomDockerPlugin implements Plugin {

    @Autowired
    private CustomDockerOperator customDockerOperator;
    @Autowired
    private CustomDockerDataOperator customDockerDataOperator;
    @Autowired
    private CustomDockerPluginInfo customDockerPluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return customDockerOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return customDockerDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(customDockerPluginInfo);
    }

}
