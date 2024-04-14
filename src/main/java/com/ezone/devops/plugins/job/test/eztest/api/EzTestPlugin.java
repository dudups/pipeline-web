package com.ezone.devops.plugins.job.test.eztest.api;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class EzTestPlugin implements Plugin {

    @Autowired
    private EzTestOperator ezTestOperator;
    @Autowired
    private EzTestDataOperator ezTestDataOperator;
    @Autowired
    private EzTestPluginInfo ezTestPluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return ezTestOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return ezTestDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(ezTestPluginInfo);
    }
}
