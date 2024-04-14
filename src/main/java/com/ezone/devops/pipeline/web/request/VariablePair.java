package com.ezone.devops.pipeline.web.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode
public class VariablePair {

    @NotBlank(message = "环境变量的key不允许为空")
    @Length(min = 1, max = 255, message = "环境变量的key最长不得超过255个字")
    private String envKey;
    @Length(min = 1, max = 255, message = "环境变量的value最长不得超过255个字")
    private String envValue;
}
