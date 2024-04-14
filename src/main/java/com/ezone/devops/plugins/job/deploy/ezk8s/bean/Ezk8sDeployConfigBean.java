package com.ezone.devops.plugins.job.deploy.ezk8s.bean;

import com.ezone.devops.pipeline.clients.request.LabelPayload;
import com.ezone.devops.plugins.job.deploy.ezk8s.model.Ezk8sDeployConfig;
import com.ezone.devops.plugins.job.enums.VersionType;
import com.ezone.galaxy.framework.common.util.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Ezk8sDeployConfigBean {

    private String clusterName;
    private String envName;
    private Long deployConfigId;
    private String deployInstanceName;
    private int replicas = 1;
    private List<LabelPayload> labels;
    private List<LabelPayload> nodeSelectors;
    private VersionType versionType;
    private String customVersion;

    public Ezk8sDeployConfigBean(Ezk8sDeployConfig ezk8SDeployConfig) {
        setClusterName(ezk8SDeployConfig.getClusterName());
        setEnvName(ezk8SDeployConfig.getEnvName());
        setDeployInstanceName(ezk8SDeployConfig.getDeployInstanceName());
        setDeployConfigId(ezk8SDeployConfig.getDeployConfigId());
        setReplicas(ezk8SDeployConfig.getReplicas());
        setLabels(JsonUtils.toObject(ezk8SDeployConfig.getLabels(),
                new TypeReference<List<LabelPayload>>() {
                }));
        setNodeSelectors(JsonUtils.toObject(ezk8SDeployConfig.getNodeSelectors(),
                new TypeReference<List<LabelPayload>>() {
                }));
        setVersionType(ezk8SDeployConfig.getVersionType());
        setCustomVersion(ezk8SDeployConfig.getCustomVersion());
    }
}
