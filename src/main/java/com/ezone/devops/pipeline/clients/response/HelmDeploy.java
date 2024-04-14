package com.ezone.devops.pipeline.clients.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class HelmDeploy {

    private String chartName;
    private String chartVersion;
    private String releaseName;
    private String logName;
    private Date createTime;
    private String releaseStatus;

}

