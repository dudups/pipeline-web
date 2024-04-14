package com.ezone.devops.plugins.job.release.service;

import com.ezone.devops.plugins.job.release.model.ArtifactReleaseBuild;

public interface ArtifactReleaseBuildService {

    ArtifactReleaseBuild getById(Long id);

    ArtifactReleaseBuild getByPipelineBuildId(Long pipelineBuildId);

    boolean hasSameReleasedVersion(ArtifactReleaseBuild artifactReleaseBuild);

    ArtifactReleaseBuild saveBuild(ArtifactReleaseBuild artifactReleaseBuild);

    boolean updateBuild(ArtifactReleaseBuild artifactReleaseBuild);

}
