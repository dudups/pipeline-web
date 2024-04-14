package com.ezone.devops.scheduler.client.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RunnerJobInfo {

    private boolean success;
    private Long taskId;
    private String logName;
    private String message;

}
