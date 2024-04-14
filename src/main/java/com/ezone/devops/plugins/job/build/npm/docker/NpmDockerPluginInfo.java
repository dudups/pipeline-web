package com.ezone.devops.plugins.job.build.npm.docker;

import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.devops.plugins.job.PluginInfoAware;
import org.springframework.stereotype.Component;

@Component
public class NpmDockerPluginInfo implements PluginInfoAware {

    public static final String JOB_TYPE = "NPM-COMPILE-DOCKER";
    public static final String NAME = "Npm构建docker镜像";
    private static final String DESCRIPTION = "使用Npm进行vue和webpack的构建，并基于构建产出自动制作docker镜像";

    @Override
    public PluginType getPluginType() {
        return PluginType.BUILD;
    }

    @Override
    public boolean customDefinitionImage() {
        return true;
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

}
