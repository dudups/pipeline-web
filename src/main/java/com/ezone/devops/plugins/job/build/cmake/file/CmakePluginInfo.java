package com.ezone.devops.plugins.job.build.cmake.file;

import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.devops.plugins.job.PluginInfoAware;
import org.springframework.stereotype.Component;

@Component
public class CmakePluginInfo implements PluginInfoAware {

    public static final String JOB_TYPE = "CMAKE-COMPILE";
    public static final String NAME = "CMake构建";
    private static final String DESCRIPTION = "使用CMake跨平台构建工具构建项目";

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
