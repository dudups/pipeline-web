package com.ezone.devops.plugins.job.build.ant.docker.bean;

import com.ezone.devops.plugins.annotation.ManualField;
import lombok.Data;

@Data
public class AntDockerBuildBean {

    @ManualField
    private boolean enableScan;

    @ManualField
    private Integer bug;
    @ManualField
    private Integer security;
    @ManualField
    private Integer style;

    @ManualField
    private Integer block;
    @ManualField
    private Integer high;
    @ManualField
    private Integer middle;
    @ManualField
    private Integer low;

    @ManualField
    private Long projectId;
    @ManualField
    private Long taskId;

}