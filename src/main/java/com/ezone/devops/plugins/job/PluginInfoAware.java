package com.ezone.devops.plugins.job;

import com.ezone.devops.plugins.enums.PluginType;

public interface PluginInfoAware {

    PluginType getPluginType();

    String getJobType();

    String getName();

    String getDescription();

    boolean customDefinitionImage();

}
