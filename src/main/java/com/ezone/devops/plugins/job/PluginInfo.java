package com.ezone.devops.plugins.job;

import com.ezone.devops.plugins.enums.PluginType;
import lombok.Data;

@Data
public class PluginInfo {

    private PluginType pluginType;
    private String jobType;
    private String name;
    private String description;
    private boolean customDefinitionImage;

    public PluginInfo(PluginInfoAware pluginInfoAware) {
        this(pluginInfoAware.getPluginType(), pluginInfoAware.getJobType(), pluginInfoAware.getName(),
                pluginInfoAware.getDescription(), pluginInfoAware.customDefinitionImage());
    }

    public PluginInfo(PluginType pluginType, String jobType, String name, String description, boolean customDefinitionImage) {
        this.pluginType = pluginType;
        this.jobType = jobType;
        this.name = name;
        this.description = description;
        this.customDefinitionImage = customDefinitionImage;
    }

}
