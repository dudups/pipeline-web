package com.ezone.devops.plugins.job.test.eztest.api;

import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.devops.plugins.job.PluginInfoAware;
import org.springframework.stereotype.Component;

@Component
public class EzTestPluginInfo implements PluginInfoAware {

    private static final String JOB_TYPE = "EZTEST";
    private static final String NAME = "内置接口测试";
    private static final String DESCRIPTION = "使用内置接口测试平台进行API测试";

    @Override
    public PluginType getPluginType() {
        return PluginType.TEST;
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
