package com.ezone.devops.pipeline.web.request;

import com.ezone.devops.pipeline.enums.CrontabTriggerType;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Validated
public class CrontabPayload {

    private String from;
    private String to;
    private String dayOfWeek;
    @Min(10)
    @Max(60)
    private Integer interval = 10;
    private String fixedTime;
    @NotNull
    private CrontabTriggerType type;

}
