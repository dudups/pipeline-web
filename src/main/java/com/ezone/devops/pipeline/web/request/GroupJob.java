package com.ezone.devops.pipeline.web.request;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class GroupJob {

    @Valid
    @NotEmpty(message = "子任务不能为空")
    private List<JobPayload> jobs;

}
