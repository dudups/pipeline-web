package com.ezone.devops.pipeline.web.request;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class RuntimeVariablePayload {

    @Valid
    @Size(min = 1, max = 100, message = "运行时环境变量数量必须在1-100之间")
    private Set<VariablePair> variables;
}
