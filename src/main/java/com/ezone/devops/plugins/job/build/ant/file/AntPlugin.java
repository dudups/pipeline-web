package com.ezone.devops.plugins.job.build.ant.file;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AntPlugin implements Plugin {

    @Autowired
    private AntOperator antOperator;
    @Autowired
    private AntDataOperator antDataOperator;
    @Autowired
    private AntPluginInfo antPluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return antOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return antDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(antPluginInfo);
    }

}
