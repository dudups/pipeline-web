package com.ezone.devops.plugins.job.test.maventest;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class MavenTestPlugin implements Plugin {

    @Autowired
    private MavenTestOperator mavenTestOperator;
    @Autowired
    private MavenTestDataOperator mavenTestDataOperator;
    @Autowired
    private MavenTestPluginInfo mavenTestPluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return mavenTestOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return mavenTestDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(mavenTestPluginInfo);
    }
}
