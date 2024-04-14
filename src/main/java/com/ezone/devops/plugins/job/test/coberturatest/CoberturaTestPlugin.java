package com.ezone.devops.plugins.job.test.coberturatest;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class CoberturaTestPlugin implements Plugin {

    @Autowired
    private CoberturaTestOperator coberturaTestOperator;
    @Autowired
    private CoberturaTestDataOperator coberturaTestDataOperator;
    @Autowired
    private CoberturaTestPluginInfo coberturaTestPluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return coberturaTestOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return coberturaTestDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(coberturaTestPluginInfo);
    }
}
