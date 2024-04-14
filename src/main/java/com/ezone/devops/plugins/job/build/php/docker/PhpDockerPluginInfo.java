package com.ezone.devops.plugins.job.build.php.docker;

import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.devops.plugins.job.PluginInfoAware;
import org.springframework.stereotype.Component;

@Component
public class PhpDockerPluginInfo implements PluginInfoAware {

    public static final String JOB_TYPE = "PHP-COMPILE-DOCKER";
    public static final String NAME = "Php构建docker镜像";
    private static final String DESCRIPTION = "基于composer构建PHP项目并基于产出自动构建docker镜像";

    @Override
    public PluginType getPluginType() {
        return PluginType.BUILD;
    }

    @Override
    public boolean customDefinitionImage() {
        return true;
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

}
