package com.ezone.devops.plugins.job.deploy.yaml.bean;

import lombok.Data;

@Data
public class K8sYamlConfigBean {

    private String clusterKey;
    private boolean useRepoFile;
    private String repoFilePath;
    private String yaml;
}
