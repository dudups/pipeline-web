package com.ezone.devops.scheduler.job;

import com.ezone.devops.plugins.enums.ArtifactRepoType;
import com.ezone.devops.plugins.enums.CloneMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class HelmPackageStep {

    private CloneMode cloneMode;
    private ArtifactRepoType pkgRepoType;
    private String pkgRepoName;

    private String chartResourcePath;

    private boolean useDefaultName;
    private String chartName;

    private boolean useDefaultVersion;
    private String customVersion;

    private boolean useDefaultAppVersion;
    private String appCustomVersion;
}
