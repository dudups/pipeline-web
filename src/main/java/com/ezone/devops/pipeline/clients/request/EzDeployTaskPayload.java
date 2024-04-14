package com.ezone.devops.pipeline.clients.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class EzDeployTaskPayload {

    private Long templateId;
    private String triggerUser;
    private String version;
    private String description;
    private String callbackUrl;
    private Set<VariablePayload> variables;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VariablePayload {
        private String variableKey;
        private String variableValue;
    }

}
