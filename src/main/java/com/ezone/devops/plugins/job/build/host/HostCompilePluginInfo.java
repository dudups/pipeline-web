package com.ezone.devops.plugins.job.build.host;

import org.springframework.stereotype.Component;

@Component
public class HostCompilePluginInfo extends AbstractHostCompilePlugin {

    public static final String JOB_TYPE = "HOST-COMPILE";
    private static final String NAME = "主机构建";
    private static final String DESCRIPTION = "用于不能使用容器的场景，只能运行在含有Machine模式主机的主机集群";

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
