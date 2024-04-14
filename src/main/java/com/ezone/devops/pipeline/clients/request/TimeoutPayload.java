package com.ezone.devops.pipeline.clients.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TimeoutPayload {

    private String jobName;
    private String jobGroup;
    private int timeoutMinute;

    public TimeoutPayload(Long pipelineBuildId, Long jobId, int timeoutMinute) {
        this.jobName = String.valueOf(pipelineBuildId);
        this.jobGroup = String.valueOf(jobId);
        this.timeoutMinute = timeoutMinute;
    }
}
