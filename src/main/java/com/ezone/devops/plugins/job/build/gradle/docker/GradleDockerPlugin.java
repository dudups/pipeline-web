package com.ezone.devops.plugins.job.build.gradle.docker;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GradleDockerPlugin implements Plugin {

    @Autowired
    private GradleDockerOperator gradleDockerOperator;
    @Autowired
    private GradleDockerDataOperator gradleDockerDataOperator;
    @Autowired
    private GradleDockerPluginInfo gradleDockerPluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return gradleDockerOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return gradleDockerDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(gradleDockerPluginInfo);
    }

}
