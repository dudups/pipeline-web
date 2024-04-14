package com.ezone.devops.pipeline.web.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class EnvConfigPayload {

    @NotBlank(message = "环境变量的key不允许为空")
    @Length(min = 1, max = 255, message = "key最长不得超过255个字")
    private String envKey;
    @Length(min = 1, max = 255, message = "value最长不得超过255个字")
    private String envValue;
    private boolean secret;
    @Length(max = 255, message = "描述最长不得超过255个字")
    private String description;

}
