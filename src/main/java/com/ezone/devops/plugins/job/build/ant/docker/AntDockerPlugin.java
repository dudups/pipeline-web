package com.ezone.devops.plugins.job.build.ant.docker;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AntDockerPlugin implements Plugin {

    @Autowired
    private AntDockerOperator antDockerOperator;
    @Autowired
    private AntDockerDataOperator antDockerDataOperator;
    @Autowired
    private AntDockerPluginInfo antDockerPluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return antDockerOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return antDockerDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(antDockerPluginInfo);
    }

}
