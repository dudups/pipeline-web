package com.ezone.devops.plugins.job.build.go.docker;

import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.devops.plugins.job.PluginInfoAware;
import org.springframework.stereotype.Component;

@Component
public class GoDockerPluginInfo implements PluginInfoAware {

    public static final String JOB_TYPE = "GO-COMPILE-DOCKER";
    public static final String NAME = "Go构建docker镜像";
    private static final String DESCRIPTION = "使用Go语言环境构建项目并基于产出自动构建docker镜像";

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
