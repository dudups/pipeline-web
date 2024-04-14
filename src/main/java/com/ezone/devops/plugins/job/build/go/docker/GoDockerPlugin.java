package com.ezone.devops.plugins.job.build.go.docker;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoDockerPlugin implements Plugin {

    @Autowired
    private GoDockerOperator goDockerOperator;
    @Autowired
    private GoDockerDataOperator goDockerDataOperator;
    @Autowired
    private GoDockerPluginInfo goDockerPluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return goDockerOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return goDockerDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(goDockerPluginInfo);
    }

}
