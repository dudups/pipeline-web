package com.ezone.devops.plugins.job.build.maven.docker;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MavenDockerPlugin implements Plugin {

    @Autowired
    private MavenDockerOperator mavenDockerOperator;
    @Autowired
    private MavenDockerDataOperator mavenDockerDataOperator;
    @Autowired
    private MavenDockerPluginInfo mavenDockerPluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return mavenDockerOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return mavenDockerDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(mavenDockerPluginInfo);
    }

}
