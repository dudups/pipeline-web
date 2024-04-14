package com.ezone.devops.pipeline.clients.request;

import lombok.Data;

@Data
public class YamlPayload {

    private Long companyId;
    private String username;
    private String yaml;

}
