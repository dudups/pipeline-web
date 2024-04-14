package com.ezone.devops.plugins.job.scan.sonarqube;

import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.devops.plugins.job.PluginInfoAware;
import org.springframework.stereotype.Component;

@Component
public class SonarqubePluginInfo implements PluginInfoAware {

    private static final String JOB_TYPE = "SONARQUBE";
    private static final String NAME = "SonarQube扫描";
    private static final String DESCRIPTION = "SonarQube扫描";

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
