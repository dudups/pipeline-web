package com.ezone.devops.pipeline.web.request;

import com.ezone.devops.pipeline.model.JobTemplate;
import com.ezone.devops.plugins.enums.PluginType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class JobTemplatePayload {

    @NotBlank(message = "插件名称不得为空")
    @Length(min = 1, max = 100, message = "插件名称长度在1到100个字符之间")
    private String jobName;
    private boolean autoBuild = true;
    @NotBlank(message = "插件类型不得为空")
    @Length(min = 1, max = 100, message = "插件类型长度在1到100个字符之间")
    private String jobType;
    @NotNull(message = "插件分类不得为空")
    private PluginType pluginType;

    public JobTemplatePayload(JobTemplate jobTemplate) {
        setJobName(jobTemplate.getJobName());
        setAutoBuild(jobTemplate.isAutoBuild());
        setJobType(jobTemplate.getJobType());
        setPluginType(jobTemplate.getPluginType());
    }
}
