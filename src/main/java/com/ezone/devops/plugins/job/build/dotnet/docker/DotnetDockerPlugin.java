package com.ezone.devops.plugins.job.build.dotnet.docker;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DotnetDockerPlugin implements Plugin {

    @Autowired
    private DotnetDockerOperator dotnetDockerOperator;
    @Autowired
    private DotnetDockerDataOperator dotnetDockerDataOperator;
    @Autowired
    private DotnetDockerPluginInfo dotnetDockerPluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return dotnetDockerOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return dotnetDockerDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(dotnetDockerPluginInfo);
    }

}
