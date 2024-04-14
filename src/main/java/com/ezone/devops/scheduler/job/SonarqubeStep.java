package com.ezone.devops.scheduler.job;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SonarqubeStep {

    private String sonarHostUrl;
    private String token;
    private boolean overrideSonarConfig;
    private String sonarProjectContent;
    private boolean enableQualityControl;
}
