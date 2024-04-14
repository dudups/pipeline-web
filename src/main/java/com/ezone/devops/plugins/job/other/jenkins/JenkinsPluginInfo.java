package com.ezone.devops.plugins.job.other.jenkins;

import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.devops.plugins.job.PluginInfoAware;
import org.springframework.stereotype.Component;

@Component
public class JenkinsPluginInfo implements PluginInfoAware {

    public static final String JOB_TYPE = "JENKINS";
    private static final String NAME = "Jenkins Job";
    private static final String DESCRIPTION = "编排和触发Jenkins任务，支持状态回传、环境变量传递至Jenkins任务参数化构建";

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
        return false;
    }
}
