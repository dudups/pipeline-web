package com.ezone.devops.plugins.job.build.python.file;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PythonPlugin implements Plugin {

    @Autowired
    private PythonOperator pythonOperator;
    @Autowired
    private PythonDataOperator pythonDataOperator;
    @Autowired
    private PythonPluginInfo pythonPluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return pythonOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return pythonDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(pythonPluginInfo);
    }

}
