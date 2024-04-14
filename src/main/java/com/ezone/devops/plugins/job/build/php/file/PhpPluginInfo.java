package com.ezone.devops.plugins.job.build.php.file;

import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.devops.plugins.job.PluginInfoAware;
import org.springframework.stereotype.Component;

@Component
public class PhpPluginInfo implements PluginInfoAware {

    public static final String JOB_TYPE = "PHP-COMPILE";
    public static final String NAME = "Php构建";
    private static final String DESCRIPTION = "安装了php运行环境和composer工具，可以为项目所依赖的php代码库提供安装和打包环境";

    @Override
    public PluginType getPluginType() {
        return PluginType.BUILD;
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
        return true;
    }

}
