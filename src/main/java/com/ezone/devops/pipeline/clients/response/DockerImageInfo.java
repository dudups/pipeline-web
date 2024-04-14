package com.ezone.devops.pipeline.clients.response;

import lombok.Data;

@Data
public class DockerImageInfo {

    private String imageUrl;
    private String repoUrl;

}
