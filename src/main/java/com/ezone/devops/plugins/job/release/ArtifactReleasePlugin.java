package com.ezone.devops.plugins.job.release;

import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class ArtifactReleasePlugin implements Plugin {

    @Autowired
    private ArtifactReleaseOperator artifactReleaseOperator;
    @Autowired
    private ArtifactReleaseDataOperator artifactReleaseDataOperator;
    @Autowired
    private ArtifactReleasePluginInfo artifactReleasePluginInfo;

    @Override
    public PluginOperator getPluginOperator() {
        return artifactReleaseOperator;
    }

    @Override
    public PluginDataOperator getPluginDataOperator() {
        return artifactReleaseDataOperator;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return new PluginInfo(artifactReleasePluginInfo);
    }
}
