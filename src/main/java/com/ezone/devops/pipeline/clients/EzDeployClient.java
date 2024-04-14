package com.ezone.devops.pipeline.clients;

import com.ezone.devops.pipeline.clients.request.EzDeployTaskPayload;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.util.HttpClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "system.deploy")
public class EzDeployClient {

    private static final String DEPLOY_URL = "/internal/tasks";

    private String platformName = "ezDeploy";
    private String endpoint;

    public BaseResponse<?> createDeployTask(EzDeployTaskPayload body) {
        return new HttpClient(getEndpoint()).path(DEPLOY_URL).jsonBody(body).retry(3).post(BaseResponse.class);
    }
}
