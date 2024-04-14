package com.ezone.devops.plugins.job.test.eztest.performance;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class EzTestPerformancePlugin implements Plugin {

    @Autowired
    private EzTestPerformanceOperator ezTestPerformanceOperator;
    @Autowired
    private EzTestPerformanceDataOperator ezTestPerformanceDataOperator;
    @Autowired
    private EzTestPerformancePluginInfo ezTestPluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return ezTestPerformanceOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return ezTestPerformanceDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(ezTestPluginInfo);
    }
}
