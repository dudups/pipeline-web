package com.ezone.devops.plugins.job.scan.ezscan;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class EzScanPlugin implements Plugin {

    @Autowired
    private EzScanOperator ezScanOperator;
    @Autowired
    private EzScanDataOperator ezScanDataOperator;
    @Autowired
    private EzScanPluginInfo ezScanPluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return ezScanOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return ezScanDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(ezScanPluginInfo);
    }
}
