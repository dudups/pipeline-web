package com.ezone.devops.plugins.job.other.docker;

import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.devops.plugins.job.PluginInfoAware;
import org.springframework.stereotype.Component;

@Component
public class DockerExecutorPluginInfo implements PluginInfoAware {

    public static final String JOB_TYPE = "DOCKER-EXECUTOR";
    private static final String NAME = "Docker镜像执行器";
    private static final String DESCRIPTION = "支持自定义镜像及脚本，可用以各种自定义任务";

    @Override
    public PluginType getPluginType() {
        return PluginType.OTHER;
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
        return false;
    }
}
