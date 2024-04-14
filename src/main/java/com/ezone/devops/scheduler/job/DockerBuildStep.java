package com.ezone.devops.scheduler.job;

import com.ezone.devops.plugins.job.enums.ArchType;
import com.ezone.devops.plugins.job.enums.PlatformType;
import com.ezone.devops.plugins.job.enums.RegistryType;
import com.ezone.devops.scheduler.bean.DockerCredential;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class DockerBuildStep {

    private List<DockerCredential> credentials;
    // 是否需要上传docker制品
    private boolean pushImage;
    private String dockerfile;
    private String dockerContext;
    private RegistryType registryType;
    private String registryUrl;
    private String dockerRepo;
    private String dockerImageName;
    private String dockerTag;
    private PlatformType platform;
    private ArchType arch;

}
