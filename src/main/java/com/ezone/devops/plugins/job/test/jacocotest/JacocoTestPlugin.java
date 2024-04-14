package com.ezone.devops.plugins.job.test.jacocotest;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class JacocoTestPlugin implements Plugin {

    @Autowired
    private JacocoTestOperator jacocoTestOperator;
    @Autowired
    private JacocoTestDataOperator jacocoTestDataOperator;
    @Autowired
    private JacocoTestPluginInfo jacocoTestPluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return jacocoTestOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return jacocoTestDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(jacocoTestPluginInfo);
    }
}
