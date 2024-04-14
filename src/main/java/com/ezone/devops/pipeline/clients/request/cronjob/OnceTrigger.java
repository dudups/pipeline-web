package com.ezone.devops.pipeline.clients.request.cronjob;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OnceTrigger {

    @ApiModelProperty("具体的执行时间")
    private long executeTimestamp;
}
