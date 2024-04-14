package com.ezone.devops.plugins.job.build.npm.file;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NpmPlugin implements Plugin {

    @Autowired
    private NpmOperator npmOperator;
    @Autowired
    private NpmDataOperator npmDataOperator;
    @Autowired
    private NpmPluginInfo npmPluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return npmOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return npmDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(npmPluginInfo);
    }

}
