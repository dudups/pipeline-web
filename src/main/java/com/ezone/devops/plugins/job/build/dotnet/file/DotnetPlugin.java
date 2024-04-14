package com.ezone.devops.plugins.job.build.dotnet.file;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DotnetPlugin implements Plugin {

    @Autowired
    private DotnetOperator dotnetOperator;
    @Autowired
    private DotnetDataOperator dotnetDataOperator;
    @Autowired
    private DotnetPluginInfo dotnetPluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return dotnetOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return dotnetDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(dotnetPluginInfo);
    }

}
