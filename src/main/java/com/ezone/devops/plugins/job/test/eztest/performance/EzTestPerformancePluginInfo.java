package com.ezone.devops.plugins.job.test.eztest.performance;

import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.devops.plugins.job.PluginInfoAware;
import org.springframework.stereotype.Component;

@Component
public class EzTestPerformancePluginInfo implements PluginInfoAware {

    private static final String JOB_TYPE = "EZTEST_PERFORMANCE";
    private static final String NAME = "内置性能测试";
    private static final String DESCRIPTION = "使用内置接口测试平台进行性能测试";

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
