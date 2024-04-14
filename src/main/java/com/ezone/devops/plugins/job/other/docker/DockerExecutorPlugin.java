package com.ezone.devops.plugins.job.other.docker;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class DockerExecutorPlugin implements Plugin {

    @Autowired
    private DockerExecutorOperator dockerExecutorOperator;
    @Autowired
    private DockerExecutorDataOperator dockerExecutorDataOperator;
    @Autowired
    private DockerExecutorPluginInfo dockerExecutorPluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return dockerExecutorOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return dockerExecutorDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(dockerExecutorPluginInfo);
    }
}
