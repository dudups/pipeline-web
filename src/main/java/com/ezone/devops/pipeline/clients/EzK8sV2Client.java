package com.ezone.devops.pipeline.clients;

import com.alibaba.fastjson.JSON;
import com.ezone.devops.pipeline.clients.request.InstallOrUpgradeRelease;
import com.ezone.devops.pipeline.clients.request.YamlPayload;
import com.ezone.devops.pipeline.clients.response.EzK8sWebClientResponse;
import com.ezone.devops.pipeline.clients.response.Ezk8sInfo;
import com.ezone.devops.pipeline.clients.response.HelmDeployResponse;
import com.ezone.devops.pipeline.clients.response.HelmTemplateResponse;
import com.ezone.devops.plugins.job.deploy.helmdeployv2.bean.HelmDeployConfigV2Bean;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.util.HttpClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@Slf4j
@ConfigurationProperties(prefix = "system.ezk8sv2")
public class EzK8sV2Client {

    private static final String CREATE_RELEASE_URL = "/internal/helm/clusters/%s/namespaces/%s/releases";
    private static final String GET_HELM_TEMPLATE_DETAIL = "/internal/helm/clusters/%s/namespaces/%s/helm_templates/%s";
    private static final String DEPLOY_YAML_URL = "/internal/manager/clusters/%s/yaml";
    private static final String BRIEF_URL = "/manager/clusters/%s/brief";
    private static final String ACCESS_TOKEN = "access_token";

    private String endpoint;
    @Value("${system.ezk8sv2.web.endpoint}")
    private String webEndpoint;
    private String toneToken;
    private String platformName = "ezK8S";

    public HelmDeployResponse createOrUpdateRelease(String clusterKey, String namespace, InstallOrUpgradeRelease release) {
        String url = String.format(CREATE_RELEASE_URL, clusterKey, namespace);
        return new HttpClient(getEndpoint()).path(url).jsonBody(release).retry(3).post(HelmDeployResponse.class);
    }

    public HelmTemplateResponse getHelmTemplate(HelmDeployConfigV2Bean helmDeployConfigV2Bean, Long companyId) {
        String url = String.format(GET_HELM_TEMPLATE_DETAIL, helmDeployConfigV2Bean.getClusterKey(), helmDeployConfigV2Bean.getNamespace(), helmDeployConfigV2Bean.getTemplateId());
        return new HttpClient(getEndpoint()).path(url).param("companyId", String.valueOf(companyId)).retry(3).get(HelmTemplateResponse.class);
    }

    public BaseResponse<?> deployYaml(String clusterKey, YamlPayload payload) {
        String url = String.format(DEPLOY_YAML_URL, clusterKey);
        return new HttpClient(getEndpoint()).path(url).jsonBody(payload).retry(3).put(BaseResponse.class);
    }

    public EzK8sWebClientResponse brief(String clusterKey) {
        String url = String.format(BRIEF_URL, clusterKey);
        EzK8sWebClientResponse ezK8sWebClientResponse = new HttpClient(webEndpoint).path(url).header(ACCESS_TOKEN,toneToken).retry(3).get(EzK8sWebClientResponse.class);
        return ezK8sWebClientResponse;
    }

}
