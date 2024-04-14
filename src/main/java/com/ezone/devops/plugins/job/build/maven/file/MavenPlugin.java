package com.ezone.devops.plugins.job.build.maven.file;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MavenPlugin implements Plugin {

    @Autowired
    private MavenOperator mavenOperator;
    @Autowired
    private MavenDataOperator mavenDataOperator;
    @Autowired
    private MavenPluginInfo mavenPluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return mavenOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return mavenDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(mavenPluginInfo);
    }

}
