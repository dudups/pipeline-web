package com.ezone.devops.plugins.job.deploy.helmdeployv2.bean;

import com.ezone.devops.plugins.annotation.ManualField;
import lombok.Data;

@Data
public class HelmDeployBuildV2Bean {

    @ManualField
    private String clusterKey;
    @ManualField
    private String namespace;
    @ManualField
    private String release;
    @ManualField
    private String chartName;
    @ManualField
    private String version;
    @ManualField
    private String logName;

}
