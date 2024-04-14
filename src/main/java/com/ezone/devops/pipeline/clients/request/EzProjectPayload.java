package com.ezone.devops.pipeline.clients.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EzProjectPayload {
    Long companyId;
    String repoName;
    Long repoId;
    String clusterKey;
    String nameSpace;
}