package com.ezone.devops.pipeline.mq.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MessageValueVo {

    private String homeFullAddress;
    private String repoName;
    private String pipelineName;
    private String pipelineId;
    private String pipelineBuildId;
    private String buildNumber;
    private String jobName;
    private String triggerUser;
    private String nickname;
    private String resultText;
}
