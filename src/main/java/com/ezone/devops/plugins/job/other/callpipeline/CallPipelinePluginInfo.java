package com.ezone.devops.plugins.job.other.callpipeline;

import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.devops.plugins.job.PluginInfoAware;
import org.springframework.stereotype.Component;

@Component
public class CallPipelinePluginInfo implements PluginInfoAware {

    private static final String JOB_TYPE = "CALL-PIPELINE";
    private static final String NAME = "触发流水线";
    private static final String DESCRIPTION = "触发其他下游流水线";

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
