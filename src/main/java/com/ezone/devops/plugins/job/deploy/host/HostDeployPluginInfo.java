package com.ezone.devops.plugins.job.deploy.host;

import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.devops.plugins.job.PluginInfoAware;
import org.springframework.stereotype.Component;

@Component
public class HostDeployPluginInfo implements PluginInfoAware {

    private static final String JOB_TYPE = "HOST-DEPLOY";
    private static final String NAME = "主机部署发单";
    private static final String DESCRIPTION = "创建主机部署任务单";

    @Override
    public PluginType getPluginType() {
        return PluginType.DEPLOY;
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
