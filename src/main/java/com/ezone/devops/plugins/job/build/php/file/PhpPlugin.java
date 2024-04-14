package com.ezone.devops.plugins.job.build.php.file;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhpPlugin implements Plugin {

    @Autowired
    private PhpOperator phpOperator;
    @Autowired
    private PhpDataOperator phpDataOperator;
    @Autowired
    private PhpPluginInfo phpPluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return phpOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return phpDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(phpPluginInfo);
    }

}
