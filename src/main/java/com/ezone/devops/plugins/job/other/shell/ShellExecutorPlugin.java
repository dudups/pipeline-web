package com.ezone.devops.plugins.job.other.shell;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class ShellExecutorPlugin implements Plugin {

    @Autowired
    private ShellExecutorPluginInfo shellExecutorPluginInfo;
    @Autowired
    private ShellExecutorOperator shellExecutorOperator;
    @Autowired
    private ShellExecutorDataOperator shellExecutorDataOperator;

    @Override
    public PluginOperator getPluginOperator() {
        return shellExecutorOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return shellExecutorDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(shellExecutorPluginInfo);
    }
}
