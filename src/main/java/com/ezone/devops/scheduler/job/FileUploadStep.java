package com.ezone.devops.scheduler.job;

import com.ezone.devops.plugins.job.build.host.model.HostCompileConfig;
import com.ezone.devops.plugins.job.other.docker.model.DockerExecutorConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadStep {

    private boolean uploadArtifact;
    private String pkgRepo;
    private String pkgName;
    private String artifactPath;
    private String artifactVersion;

    public FileUploadStep(HostCompileConfig hostCompileConfig, String artifactVersion) {
        setUploadArtifact(hostCompileConfig.isUploadArtifact());
        if (hostCompileConfig.isUploadArtifact()) {
            setPkgRepo(hostCompileConfig.getPkgRepo());
            setPkgName(hostCompileConfig.getPkgName());
            setArtifactPath(hostCompileConfig.getArtifactPath());
            setArtifactVersion(artifactVersion);
        }
    }

    public FileUploadStep(DockerExecutorConfig dockerExecutorConfig, String artifactVersion) {
        setUploadArtifact(dockerExecutorConfig.isUploadArtifact());
        if (dockerExecutorConfig.isUploadArtifact()) {
            setPkgRepo(dockerExecutorConfig.getPkgRepo());
            setPkgName(dockerExecutorConfig.getPkgName());
            setArtifactPath(dockerExecutorConfig.getArtifactPath());
            setArtifactVersion(artifactVersion);
        }
    }
}
