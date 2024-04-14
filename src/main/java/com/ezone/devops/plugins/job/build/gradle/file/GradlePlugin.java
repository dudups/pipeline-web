package com.ezone.devops.plugins.job.build.gradle.file;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GradlePlugin implements Plugin {

    @Autowired
    private GradleOperator gradleOperator;
    @Autowired
    private GradleDataOperator gradleDataOperator;
    @Autowired
    private GradlePluginInfo gradlePluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return gradleOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return gradleDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(gradlePluginInfo);
    }

}
