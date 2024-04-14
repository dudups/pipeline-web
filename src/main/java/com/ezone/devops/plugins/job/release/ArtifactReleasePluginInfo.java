package com.ezone.devops.plugins.job.release;

import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.devops.plugins.job.PluginInfoAware;
import org.springframework.stereotype.Component;

@Component
public class ArtifactReleasePluginInfo implements PluginInfoAware {

    public static final String JOB_TYPE = "ARTIFACT-RELEASE";
    private static final String NAME = "发布版本";
    private static final String DESCRIPTION = "生成发布版本号、创建代码库Tag、自动归档file、APK、IPA、Docker半成品至成品库；其他类型需自行发布成品";

    @Override
    public PluginType getPluginType() {
        return PluginType.RELEASE;
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
        return false;
    }
}
