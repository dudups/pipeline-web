package com.ezone.devops.pipeline.clients.request.cronjob;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class FixedTrigger {

    @ApiModelProperty("需要执行的星期几，仅支持：1,2,3,4,5,6,7")
    private Set<Integer> dayOfWeek;
    @ApiModelProperty("固定时间执行, 例：13:00")
    private String fixedTime;

}
