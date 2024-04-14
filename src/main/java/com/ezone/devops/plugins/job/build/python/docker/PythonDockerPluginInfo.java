package com.ezone.devops.plugins.job.build.python.docker;

import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.devops.plugins.job.PluginInfoAware;
import org.springframework.stereotype.Component;

@Component
public class PythonDockerPluginInfo implements PluginInfoAware {

    public static final String JOB_TYPE = "PYTHON-COMPILE-DOCKER";
    public static final String NAME = "python构建docker镜像";
    private static final String DESCRIPTION = "使用Pyinstaller构建Python项目并基于产出自动构建Docker镜像";

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
