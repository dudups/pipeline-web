package com.ezone.devops.plugins.job.build.helm;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class HelmPackagePlugin implements Plugin {

    @Autowired
    private HelmPackagePluginInfo helmPackagePluginInfo;
    @Autowired
    private HelmPackageOperator helmPackageOperator;
    @Autowired
    private HelmPackageDataOperator helmPackageDataOperator;

    @Override
    public PluginOperator getPluginOperator() {
        return helmPackageOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return helmPackageDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(helmPackagePluginInfo);
    }
}
