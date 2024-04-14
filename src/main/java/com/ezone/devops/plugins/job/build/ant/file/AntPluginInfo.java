package com.ezone.devops.plugins.job.build.ant.file;

import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.devops.plugins.job.PluginInfoAware;
import org.springframework.stereotype.Component;

@Component
public class AntPluginInfo implements PluginInfoAware {

    public static final String JOB_TYPE = "ANT-COMPILE";
    public static final String NAME = "Ant构建";
    private static final String DESCRIPTION = "用Ant构建项目";

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
