package com.ezone.devops.scheduler.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class DockerCredential {

    private String registryUrl;
    private String username;
    private String token;
}
