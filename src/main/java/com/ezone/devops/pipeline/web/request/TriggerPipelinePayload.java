package com.ezone.devops.pipeline.web.request;

import com.ezone.devops.pipeline.common.ScmTriggerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TriggerPipelinePayload {

    private ScmTriggerType scmTriggerType = ScmTriggerType.BRANCH;
    @NotBlank(message = "分支、tag、commit之类的名称不能为空")
    private String externalName;
    @Valid
    @Size(max = 100, message = "运行时环境变量数量必须在1-100之间")
    private Set<VariablePair> variables;

}
