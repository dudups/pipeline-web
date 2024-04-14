package com.ezone.devops.pipeline.clients.request;

import com.ezone.devops.plugins.job.scan.sonarqube.model.SonarqubeBuild;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
public class SonarqubePayload {

    private String branchName;
    private String commitId;
    private String dashboardUrl = StringUtils.EMPTY;
    private Integer vulnerabilities;
    private Integer bugs;
    private Integer codeSmells;

    public SonarqubePayload(SonarqubeBuild sonarqubeBuild, String branchName, String commitId) {
        BeanUtils.copyProperties(sonarqubeBuild, this);
        setBranchName(branchName);
        setCommitId(commitId);
    }
}
