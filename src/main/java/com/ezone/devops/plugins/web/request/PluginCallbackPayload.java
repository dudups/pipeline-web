package com.ezone.devops.plugins.web.request;

import com.ezone.devops.pipeline.common.JobEvent;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class PluginCallbackPayload {

    @NotNull(message = "job事件不能为空")
    private JobEvent jobEvent;
    @Length(max = 255, message = "消息长度不能超过255个字符")
    private String message = StringUtils.EMPTY;
    private Object data;
}
