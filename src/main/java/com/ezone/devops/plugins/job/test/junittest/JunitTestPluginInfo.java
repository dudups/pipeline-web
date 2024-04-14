package com.ezone.devops.plugins.job.test.junittest;

import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.devops.plugins.job.PluginInfoAware;
import org.springframework.stereotype.Component;

@Component
public class JunitTestPluginInfo implements PluginInfoAware {

    private static final String JOB_TYPE = "JUNIT-TEST";
    private static final String NAME = "JUnit测试结果采集";
    private static final String DESCRIPTION = "jUnit测试结果采集，并进行相关失败规则门禁配置";

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
