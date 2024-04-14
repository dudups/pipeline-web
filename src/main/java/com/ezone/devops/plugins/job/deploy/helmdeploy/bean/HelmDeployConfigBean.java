package com.ezone.devops.plugins.job.deploy.helmdeploy.bean;

import com.ezone.devops.plugins.job.enums.VersionType;
import lombok.Data;

@Data
public class HelmDeployConfigBean {

    private String clusterKey;
    private String namespace;
    private String repoName;
    private String repoType;
    private String chartName;
    private String releaseName;
    private VersionType versionType;
    private String customVersion;
    private String chartValues;
    private boolean atomic;
    private boolean wait;
    private long timeout = 300;

}
