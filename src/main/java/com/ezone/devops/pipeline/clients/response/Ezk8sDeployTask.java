package com.ezone.devops.pipeline.clients.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Ezk8sDeployTask {

    private Long id;
    private Long companyId;
    private Long clusterId;
    private Long envId;
    private Long deployConfigId;
    private String triggerUser;
    private String deployConfigName;
    private String deployInstanceName;
    private String dockerTag;
    private int replicas;
    private String approvalMessage;
    private String description;
    private Date createTime;

}
