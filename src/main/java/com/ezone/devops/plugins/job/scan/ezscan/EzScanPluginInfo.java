package com.ezone.devops.plugins.job.scan.ezscan;

import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.devops.plugins.job.PluginInfoAware;
import org.springframework.stereotype.Component;

@Component
public class EzScanPluginInfo implements PluginInfoAware {

    private static final String JOB_TYPE = "EZSCAN";
    private static final String NAME = "内置代码扫描";
    private static final String DESCRIPTION = "用系统内置代码扫描平台进行代码扫描";

    @Override
    public PluginType getPluginType() {
        return PluginType.SCAN;
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
