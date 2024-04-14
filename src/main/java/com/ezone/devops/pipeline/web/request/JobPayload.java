package com.ezone.devops.pipeline.web.request;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.enums.ConditionTriggerType;
import com.ezone.devops.pipeline.enums.JobConditionType;
import com.ezone.devops.plugins.enums.PluginType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class JobPayload {

    @NotBlank(message = "插件的名称不允许为空")
    @Length(min = 1, max = 100, message = "插件名称长度在1到100个字符之间")
    private String jobName;
    @NotBlank(message = "插件的类型不允许为空")
    @Length(min = 1, max = 100, message = "插件类型长度在1到100个字符之间")
    private String jobType;
    @NotNull(message = "插件的分类不允许为空")
    private PluginType pluginType;

    @ApiModelProperty("job条件策略")
    @NotNull(message = "job的条件策略不允许为空")
    private JobConditionType conditionType = JobConditionType.AUTO;
    @ApiModelProperty("条件匹配后，执行job的策略")
    private ConditionTriggerType conditionTriggerType = ConditionTriggerType.EXECUTE;
    @Valid
    @ApiModelProperty("条件匹配的环境变量")
    private Set<ConditionVariablePair> conditionVariable;

    private JSONObject realJob = new JSONObject();

}
