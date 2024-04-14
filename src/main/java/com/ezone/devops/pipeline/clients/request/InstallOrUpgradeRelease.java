package com.ezone.devops.pipeline.clients.request;

import com.ezone.devops.plugins.job.enums.RepoType;
import lombok.Data;

@Data
public class InstallOrUpgradeRelease {

    private Long companyId;
    private String username;
    private String repoName;
    private RepoType repoType;
    private String chartName;
    private String release;
    private String version;
    private String values;
    private boolean atomic = false;
    private boolean wait = false;
    private long timeout = 300;
    private String callbackUrl;

}
