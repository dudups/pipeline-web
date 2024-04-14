package com.ezone.devops.plugins.job.build.gradle.file;

import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.devops.plugins.job.PluginInfoAware;
import org.springframework.stereotype.Component;

@Component
public class GradlePluginInfo implements PluginInfoAware {

    public static final String JOB_TYPE = "GRADLE-COMPILE";
    public static final String NAME = "Gradle构建";
    private static final String DESCRIPTION = "使用Gradle构建工具构建Java，Groovy和Scala项目";

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
