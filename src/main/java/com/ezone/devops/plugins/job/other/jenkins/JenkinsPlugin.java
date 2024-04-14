package com.ezone.devops.plugins.job.other.jenkins;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JenkinsPlugin implements Plugin {

    @Autowired
    private JenkinsPluginInfo jenkinsPluginInfo;
    @Autowired
    private JenkinsJobExecutor jenkinsJobExecutor;
    @Autowired
    private JenkinsJobDataOperator jenkinsJobDataOperator;

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(jenkinsPluginInfo);
    }

    @Override
    public PluginOperator getPluginOperator() {
        return jenkinsJobExecutor;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return jenkinsJobDataOperator;
    }
}
