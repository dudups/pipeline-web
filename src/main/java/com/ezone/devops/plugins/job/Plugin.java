package com.ezone.devops.plugins.job;

public interface Plugin {

    PluginInfo getPluginInfo();

    PluginOperator getPluginOperator();

    PluginDataOperator getPluginDataOperator();

}
