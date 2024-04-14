package com.ezone.devops.plugins.job.build.go.file;

import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.devops.plugins.job.PluginInfoAware;
import org.springframework.stereotype.Component;

@Component
public class GoPluginInfo implements PluginInfoAware {

    public static final String JOB_TYPE = "GO-COMPILE";
    public static final String NAME = "Go构建";
    private static final String DESCRIPTION = "使用Go语言环境构建";

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
