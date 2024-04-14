package com.ezone.devops.plugins.job.scan.sonarqube;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class SonarqubePlugin implements Plugin {

    @Autowired
    private SonarqubeOperator sonarqubeOperator;
    @Autowired
    private SonarqubeDataOperator sonarqubeDataOperator;
    @Autowired
    private SonarqubePluginInfo sonarqubePluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return sonarqubeOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return sonarqubeDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(sonarqubePluginInfo);
    }
}
