package com.ezone.devops.plugins.job.build.dotnet.file;

import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.devops.plugins.job.PluginInfoAware;
import org.springframework.stereotype.Component;

@Component
public class DotnetPluginInfo implements PluginInfoAware {

    public static final String JOB_TYPE = "DOTNET-COMPILE";
    public static final String NAME = ".Net构建";
    private static final String DESCRIPTION = "使用.Net平台工具构建项目";

    @Override
    public PluginType getPluginType() {
        return PluginType.BUILD;
    }

    @Override
    public String getJobType() {
        return JOB_TYPE;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public boolean customDefinitionImage() {
        return true;
    }

}
