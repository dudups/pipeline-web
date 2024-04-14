package com.ezone.devops.pipeline.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "system.config")
public class SystemConfig {

    private String defaultClusterName = "系统内置";
    private int threadCount = 30;
    private int reExecuteLimit = 2;
    private String buildImagePrefix;
}
