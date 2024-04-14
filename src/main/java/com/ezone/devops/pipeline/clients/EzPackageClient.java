package com.ezone.devops.pipeline.clients;

import com.ezone.devops.pipeline.clients.request.ArtifactReleasePayload;
import com.ezone.devops.pipeline.clients.response.DockerImageInfoResponse;
import com.ezone.devops.plugins.job.enums.ImageVersionType;
import com.ezone.devops.plugins.job.enums.RegistryType;
import com.ezone.devops.plugins.job.other.docker.model.DockerExecutorConfig;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.util.HttpClient;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "system.package")
public class EzPackageClient {

    private static final String RELEASE_URL = "/internal/artifact/batchRelease";
    private final static String DOCKER_INFO_URL = "/internal/artifact/docker/hosted";
    private final static String MIRROR_DOCKER_INFO_URL = "/internal/artifact/docker/mirror";

    private String endpoint;
    private String platformName = "ezPackage";

    public BaseResponse<?> releaseArtifact(ArtifactReleasePayload artifactReleasePayload) {
        log.info("start invoke ezpkg release artifact,params:[{}]", artifactReleasePayload);
        BaseResponse<?> response = new HttpClient(getEndpoint()).path(RELEASE_URL).jsonBody(artifactReleasePayload).retry(3)
                .post(BaseResponse.class, false).getData();
        log.info("finished invoke ezpkg release artifact,params:[{}]", artifactReleasePayload);
        return response;
    }

    public DockerImageInfoResponse getDockerImageInfo(Long companyId, DockerExecutorConfig dockerExecutorConfig) {
        RegistryType registryType = dockerExecutorConfig.getRegistryType();
        if (registryType == RegistryType.INTERNAL_PKG) {
            return queryDockerImageInfo(dockerExecutorConfig.getRegistryId(), dockerExecutorConfig.getImageName(),
                    dockerExecutorConfig.getVersionType(), dockerExecutorConfig.getVersion());
        } else {
            return queryMirrorDockerImageInfo(companyId, dockerExecutorConfig.getImageName(), dockerExecutorConfig.getVersion());
        }

    }

    private DockerImageInfoResponse queryDockerImageInfo(Long registryId, String imageName, ImageVersionType versionType,
                                                         String version) {
        Map<String, String> params = Maps.newHashMap();
        params.put("repoId", String.valueOf(registryId));
        params.put("pkgName", imageName);
        switch (versionType) {
            case FIXED: {
                params.put("version", version);
                break;
            }
            case LATEST: {
                params.put("isNewest", String.valueOf(true));
                break;
            }
        }
        return new HttpClient(getEndpoint()).path(DOCKER_INFO_URL).param(params).retry(3)
                .get(DockerImageInfoResponse.class);
    }

    private DockerImageInfoResponse queryMirrorDockerImageInfo(Long companyId, String imageName, String version) {
        Map<String, String> params = Maps.newHashMap();
        params.put("companyId", String.valueOf(companyId));
        params.put("pkgName", imageName);
        params.put("version", version);
        return new HttpClient(getEndpoint()).path(MIRROR_DOCKER_INFO_URL).param(params).retry(3)
                .get(DockerImageInfoResponse.class);
    }
}
