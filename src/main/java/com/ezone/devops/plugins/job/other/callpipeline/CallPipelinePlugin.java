package com.ezone.devops.plugins.job.other.callpipeline;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class CallPipelinePlugin implements Plugin {

    @Autowired
    private CallPipelinePluginInfo callPipelinePluginInfo;
    @Autowired
    private CallPipelineOperator callPipelineOperator;
    @Autowired
    private CallPipelineDataOperator callPipelineDataOperator;

    @Override
    public PluginOperator getPluginOperator() {
        return callPipelineOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return callPipelineDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(callPipelinePluginInfo);
    }
}
