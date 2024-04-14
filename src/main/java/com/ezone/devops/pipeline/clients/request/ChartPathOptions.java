package com.ezone.devops.pipeline.clients.request;

import lombok.Data;

@Data
public class ChartPathOptions {
    private String caFile;
    private String certFile;
    private String keyFile;
    private boolean insecureSkipTLSverify;
    private String keyring;
    private String password;
    private String repoURL;
    private String username;
    private boolean verify;
    private String version;
}
