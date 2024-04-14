package com.ezone.devops.plugins.job.build.host;

import org.springframework.stereotype.Component;

@Component
public class AndroidHostCompilePluginInfo extends AbstractHostCompilePlugin {

    public static final String JOB_TYPE = "ANDROID-COMPILE";
    private static final String NAME = "Android构建";
    private static final String DESCRIPTION = "使用Android构建系统构建APK,只能运行在含有Machine模式的主机集群";

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
