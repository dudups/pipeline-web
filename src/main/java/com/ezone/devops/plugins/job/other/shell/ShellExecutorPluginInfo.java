package com.ezone.devops.plugins.job.other.shell;

import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.devops.plugins.job.PluginInfoAware;
import org.springframework.stereotype.Component;

@Component
public class ShellExecutorPluginInfo implements PluginInfoAware {

    private static final String JOB_TYPE = "SHELL-EXECUTOR";
    private static final String NAME = "Shell命令";
    private static final String DESCRIPTION = "支持Shell脚本，预置linux系统及kubectl、helm、JDK、PHP";

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
        return true;
    }
}
