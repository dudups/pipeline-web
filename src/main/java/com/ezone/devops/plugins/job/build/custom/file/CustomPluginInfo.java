package com.ezone.devops.plugins.job.build.custom.file;

import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.devops.plugins.job.PluginInfoAware;
import org.springframework.stereotype.Component;

@Component
public class CustomPluginInfo implements PluginInfoAware {

    public static final String JOB_TYPE = "CUSTOM-COMPILE";
    public static final String NAME = "代码打包";
    private static final String DESCRIPTION = "仅对源代码打包，不进行实际编译，适合脚本类语言";

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
