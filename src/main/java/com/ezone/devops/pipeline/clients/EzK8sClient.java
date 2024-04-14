package com.ezone.devops.pipeline.clients;

import com.ezone.devops.pipeline.clients.request.DeployTaskPayload;
import com.ezone.devops.pipeline.clients.response.Ezk8sDeployTaskResponse;
import com.ezone.devops.pipeline.clients.response.Ezk8sInfo;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.util.HttpClient;
import com.google.common.collect.Maps;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "system.ezk8s")
public class EzK8sClient {

    private static final String CLUSTERS_URL = "/internal/clusters";
    private static final String ENV_URL = "/internal/clusters/%s/envs";
    private static final String DEPLOY_CONFIG_URL = "/internal/clusters/%s/envs/%s/deploy_configs";
    private static final String DEPLOY_TASK_URL = "/internal/clusters/%s/envs/%s/deploy_tasks";
    private static final String COMPANY_ID = "companyId";
    private static final String NAME = "name";
    private String endpoint;
    private String platformName = "ezK8S";

    public BaseResponse<?> suggestClusterByName(Long companyId, String name) {
        Map<String, String> params = generateParams(companyId, name);
        return new HttpClient(getEndpoint()).path(CLUSTERS_URL).param(params).retry(3).get(BaseResponse.class);
    }

    public BaseResponse<?> listEnvs(Long companyId, String clusterName) {
        String url = String.format(ENV_URL, clusterName);
        return new HttpClient(getEndpoint()).path(url).param(COMPANY_ID, String.valueOf(companyId))
                .retry(3).get(BaseResponse.class);
    }

    public BaseResponse<?> suggestDeployConfigs(Long companyId, String clusterName, String envName, String name) {
        String url = String.format(DEPLOY_CONFIG_URL, clusterName, envName);
        Map<String, String> params = generateParams(companyId, name);
        return new HttpClient(getEndpoint()).path(url).param(params).retry(3).get(BaseResponse.class);
    }

    public Ezk8sDeployTaskResponse createDeployTask(String clusterName, String envName,
                                                    DeployTaskPayload deployTaskPayload) {
        String url = String.format(DEPLOY_TASK_URL, clusterName, envName);
        return new HttpClient(getEndpoint()).path(url).jsonBody(deployTaskPayload).retry(3)
                .post(Ezk8sDeployTaskResponse.class);
    }

    private Map<String, String> generateParams(Long companyId, String name) {
        Map<String, String> params = Maps.newHashMap();
        params.put(COMPANY_ID, String.valueOf(companyId));
        if (StringUtils.isNotBlank(name)) {
            params.put(NAME, name);
        }
        return params;
    }
}
