package com.ezone.devops.plugins.job.build.cmake.docker;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CmakeDockerPlugin implements Plugin {

    @Autowired
    private CmakeDockerOperator cmakeDockerOperator;
    @Autowired
    private CmakeDockerDataOperator cmakeDockerDataOperator;
    @Autowired
    private CmakeDockerPluginInfo cmakeDockerPluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return cmakeDockerOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return cmakeDockerDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(cmakeDockerPluginInfo);
    }

}
