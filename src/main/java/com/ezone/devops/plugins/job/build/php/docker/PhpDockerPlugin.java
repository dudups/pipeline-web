package com.ezone.devops.plugins.job.build.php.docker;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PhpDockerPlugin implements Plugin {

    @Autowired
    private PhpDockerOperator phpDockerOperator;
    @Autowired
    private PhpDockerDataOperator phpDockerDataOperator;
    @Autowired
    private PhpDockerPluginInfo phpDockerPluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return phpDockerOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return phpDockerDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(phpDockerPluginInfo);
    }

}
