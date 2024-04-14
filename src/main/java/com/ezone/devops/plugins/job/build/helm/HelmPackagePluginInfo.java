package com.ezone.devops.plugins.job.build.helm;

import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.devops.plugins.job.PluginInfoAware;
import org.springframework.stereotype.Component;

@Component
public class HelmPackagePluginInfo implements PluginInfoAware {

    public static final String JOB_TYPE = "HELM-PACKAGE";
    private static final String NAME = "Helm构建";
    private static final String DESCRIPTION = "将chart文件打包至Helm库";

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
