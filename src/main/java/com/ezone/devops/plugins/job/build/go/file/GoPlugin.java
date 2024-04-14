package com.ezone.devops.plugins.job.build.go.file;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoPlugin implements Plugin {

    @Autowired
    private GoOperator goOperator;
    @Autowired
    private GoDataOperator goDataOperator;
    @Autowired
    private GoPluginInfo goPluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return goOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return goDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(goPluginInfo);
    }

}
