package com.ezone.devops.plugins.job.build.npm.docker;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NpmDockerPlugin implements Plugin {

    @Autowired
    private NpmDockerOperator npmDockerOperator;
    @Autowired
    private NpmDockerDataOperator npmDockerDataOperator;
    @Autowired
    private NpmDockerPluginInfo npmDockerPluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return npmDockerOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return npmDockerDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(npmDockerPluginInfo);
    }

}
