package com.ezone.devops.pipeline.mq.bean;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RepoMessage {

    private Long companyId;
    private Long repoId;
    private String repoName;

}
