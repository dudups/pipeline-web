package com.ezone.devops.pipeline.web.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PipelineSettingPayload {


    @ApiModelProperty("流水线记录自动清理时间")
    @Min(value = 15, message = "最小不得小于15天")
    @Max(value = 9999, message = "最大不的超过9999天")
    private int recordExpireDay = 180;

    @ApiModelProperty("报告自动清理时间")
    @Min(value = 1, message = "最小不得小于一天")
    @Max(value = 3650, message = "最大不的超过3650天")
    private int reportExpireDay = 30;

}
