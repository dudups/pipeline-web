package com.ezone.devops.pipeline.clients.response;

import com.ezone.devops.plugins.job.enums.RepoType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class HelmTemplate {

    private String creator;
    private String name;
    private String releaseName;
    private String repoName;
    private RepoType repoType;
    private String chartName;
    private String chartValues;
    private Date modifyTime;
    private boolean atomic;
    private long timeout;
    private boolean wait;

}
