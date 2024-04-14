package com.ezone.devops.pipeline.clients.request.cronjob;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HttpCron<T> {

    @ApiModelProperty("任务的标识")
    private JobIdentity jobIdentity;
    @ApiModelProperty("执行的周期")
    private T trigger;
    @ApiModelProperty("回调相关信息，必须是内部可访问的接口")
    private HttpJobData httpJobData;

}
