package com.ezone.devops.plugins.job.build.custom.file;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomPlugin implements Plugin {

    @Autowired
    private CustomOperator customOperator;
    @Autowired
    private CustomDataOperator customDataOperator;
    @Autowired
    private CustomPluginInfo customPluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return customOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return customDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(customPluginInfo);
    }

}
