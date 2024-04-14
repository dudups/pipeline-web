package com.ezone.devops.plugins.job.other.jenkins.bean;

import lombok.Data;

@Data
public class JenkinsCallback {

    private String dashboardUrl;
    private Long buildNumber;
    private String buildStatus;
    private Long startTime;
    private Long endTime;

}

