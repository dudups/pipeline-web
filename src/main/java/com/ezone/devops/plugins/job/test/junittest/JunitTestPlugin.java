package com.ezone.devops.plugins.job.test.junittest;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class JunitTestPlugin implements Plugin {

    @Autowired
    private JunitTestOperator junitTestOperator;
    @Autowired
    private JunitTestDataOperator junitTestDataOperator;
    @Autowired
    private JunitTestPluginInfo junitTestPluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return junitTestOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return junitTestDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(junitTestPluginInfo);
    }
}
