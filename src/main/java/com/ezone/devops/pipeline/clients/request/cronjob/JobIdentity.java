package com.ezone.devops.pipeline.clients.request.cronjob;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JobIdentity {
    @ApiModelProperty("调用的系统")
    private String systemType;
    @ApiModelProperty("任务的名称，对接系统内唯一，需要下游系统自己保证，如果不已经存在，系统会报错")
    private String jobName;
}
