package com.ezone.devops.pipeline.clients.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Ezk8sInfo {
    private Long id;
    private String clusterKey;
    private String name;
    private String remark;
    private Boolean favourite;
    private String status;
}
