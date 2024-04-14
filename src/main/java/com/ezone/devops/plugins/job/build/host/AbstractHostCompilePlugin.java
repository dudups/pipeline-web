package com.ezone.devops.plugins.job.build.host;

import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.devops.plugins.job.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

@Lazy
public abstract class AbstractHostCompilePlugin implements PluginInfoAware, Plugin {

    @Autowired
    private HostCompileOperator hostCompileOperator;
    @Autowired
    private HostCompileDataOperator hostCompileDataOperator;

    @Override
    public PluginType getPluginType() {
        return PluginType.BUILD;
    }

    @Override
    public boolean customDefinitionImage() {
        return false;
    }

    @Override
    public PluginOperator getPluginOperator() {
        return hostCompileOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return hostCompileDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(getPluginType(), getJobType(), getName(), getDescription(), customDefinitionImage());
    }

}
