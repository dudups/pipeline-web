package com.ezone.devops.plugins.job.build.npm.file;

import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.devops.plugins.job.PluginInfoAware;
import org.springframework.stereotype.Component;

@Component
public class NpmPluginInfo implements PluginInfoAware {

    public static final String JOB_TYPE = "NPM-COMPILE";
    public static final String NAME = "Npm构建";
    private static final String DESCRIPTION = "使用Npm工具管理软件包,能做vue和webpack的构建";

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
