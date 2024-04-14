package com.ezone.devops.plugins.job.build.custom.docker;

import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.devops.plugins.job.PluginInfoAware;
import org.springframework.stereotype.Component;

@Component
public class CustomDockerPluginInfo implements PluginInfoAware {

    public static final String JOB_TYPE = "CUSTOM-COMPILE-DOCKER";
    public static final String NAME = "代码打包docker镜像";
    private static final String DESCRIPTION = "仅对源代码打包，并基于打包产物自动构建docker镜像";

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
