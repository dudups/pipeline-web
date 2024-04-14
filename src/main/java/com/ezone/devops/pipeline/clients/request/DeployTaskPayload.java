package com.ezone.devops.pipeline.clients.request;

import com.ezone.devops.plugins.job.deploy.ezk8s.model.Ezk8sDeployBuild;
import com.ezone.devops.plugins.job.deploy.ezk8s.model.Ezk8sDeployConfig;
import com.ezone.galaxy.framework.common.util.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class DeployTaskPayload {

    private Long companyId;
    private String username;
    private String deployInstanceName;
    private Long deployConfigId;
    private String dockerTag;
    private int replicas = 1;
    private List<LabelPayload> labels;
    private List<LabelPayload> nodeSelectors;
    private String description;

    public DeployTaskPayload(Long companyId, String triggerUser, String dockerTag,
                             Ezk8sDeployConfig ezk8SDeployConfig, Ezk8sDeployBuild ezk8SDeployBuild) {
        setCompanyId(companyId);
        setUsername(triggerUser);

        setDeployInstanceName(ezk8SDeployConfig.getDeployInstanceName());
        setDeployConfigId(ezk8SDeployConfig.getDeployConfigId());
        setDockerTag(dockerTag);
        setReplicas(ezk8SDeployConfig.getReplicas());
        setLabels(JsonUtils.toObject(ezk8SDeployConfig.getLabels(), new TypeReference<List<LabelPayload>>() {
        }));
        setNodeSelectors(JsonUtils.toObject(ezk8SDeployConfig.getNodeSelectors(),
                new TypeReference<List<LabelPayload>>() {
                }));
        setDescription(ezk8SDeployBuild.getDescription());
    }
}
