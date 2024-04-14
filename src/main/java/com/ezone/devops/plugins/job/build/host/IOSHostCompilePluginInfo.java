package com.ezone.devops.plugins.job.build.host;

import org.springframework.stereotype.Component;

@Component
public class IOSHostCompilePluginInfo extends AbstractHostCompilePlugin {

    public static final String JOB_TYPE = "IOS-COMPILE";
    private static final String NAME = "IOS构建";
    private static final String DESCRIPTION = "在IOS主机环境下构建并生成IPA文件，只能运行在含有Machine模式Mac主机的主机集群";

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
