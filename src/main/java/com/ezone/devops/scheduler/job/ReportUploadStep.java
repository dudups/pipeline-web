package com.ezone.devops.scheduler.job;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ReportUploadStep {

    private String reportId;

    private boolean uploadReport;
    private String reportDir;
    private String reportIndex;

    private boolean autoGenerateConfig;
    private String userHomePath;
    private Set<String> publicRepoNames;
    private Set<String> privateRepoNames;

}
