package com.ezone.devops.plugins.job.build.python.docker;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PythonDockerPlugin implements Plugin {

    @Autowired
    private PythonDockerOperator pythonDockerOperator;
    @Autowired
    private PythonDockerDataOperator pythonDockerDataOperator;
    @Autowired
    private PythonDockerPluginInfo pythonDockerPluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return pythonDockerOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return pythonDockerDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(pythonDockerPluginInfo);
    }

}
