package com.ezone.devops.pipeline.clients.request.cronjob;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class DailyTrigger {

    @ApiModelProperty("开始时间, 例：13:00")
    private String from;
    @ApiModelProperty("结束时间, 例：14:00")
    private String to;
    @ApiModelProperty("需要执行的星期几，仅支持：1,2,3,4,5,6,7")
    private Set<Integer> dayOfWeek;
    @ApiModelProperty("执行间隔, 分钟")
    private int interval;
}
