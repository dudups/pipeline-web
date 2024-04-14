package com.ezone.devops.report.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Data
@Configuration
@ConfigurationProperties(prefix = "report.config")
public class ReportSystemConfig {

    private String basePath;
    private Set<String> staticSuffix;

}
