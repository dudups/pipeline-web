package com.ezone.devops.pipeline.clients.request;

import com.ezone.devops.pipeline.enums.ResourceType;
import com.ezone.devops.plugins.enums.ScanLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class EzScanPayload {

    private String companyName;
    private Long companyId;
    private String repoId;
    private String branch;
    private String baseCommit;
    private String commit;
    private String creator;
    private Set<String> filterDirs;
    private Long rulesetId;
    private ScanLevel scanLevel;
    private int timeoutSpan;
    private Boolean isDotNetFramework;
    private Boolean isXcodeApp;

    private ResourceType resourceType;
    private String resourceName;
    private String callbackUrl;

}
