package com.ezone.devops.plugins.job.build.cmake.file;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CmakePlugin implements Plugin {

    @Autowired
    private CmakeOperator cmakeOperator;
    @Autowired
    private CmakeDataOperator cmakeDataOperator;
    @Autowired
    private CmakePluginInfo cmakePluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return cmakeOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return cmakeDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(cmakePluginInfo);
    }

}
