package com.ezone.devops.plugins.job.build.maven.file;

import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.devops.plugins.job.PluginInfoAware;
import org.springframework.stereotype.Component;

@Component
public class MavenPluginInfo implements PluginInfoAware {

    public static final String JOB_TYPE = "MAVEN-COMPILE";
    public static final String NAME = "Maven构建";
    private static final String DESCRIPTION = "使用Apache Maven构建Java项目";

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
