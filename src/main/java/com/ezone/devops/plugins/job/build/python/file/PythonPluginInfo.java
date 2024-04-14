package com.ezone.devops.plugins.job.build.python.file;

import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.devops.plugins.job.PluginInfoAware;
import org.springframework.stereotype.Component;

@Component
public class PythonPluginInfo implements PluginInfoAware {

    public static final String JOB_TYPE = "PYTHON-COMPILE";
    public static final String NAME = "python构建";
    private static final String DESCRIPTION = "使用Pyinstaller工具构建Python项目";

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
