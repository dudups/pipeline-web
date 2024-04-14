package com.ezone.devops.plugins.service;

import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.plugins.enums.ArtifactType;
import com.ezone.devops.plugins.job.release.beans.ReleaseArtifact;
import com.ezone.devops.plugins.model.ArtifactInfo;

import java.util.Collection;
import java.util.List;

public interface ArtifactInfoService {

    ArtifactInfo saveArtifactInfo(PipelineRecord pipelineRecord, ArtifactType artifactType, String pkgRepo,
                                  String pkgName, String version);

    boolean updateArtifactInfo(PipelineRecord pipelineRecord, Collection<ReleaseArtifact> releaseArtifacts);

    boolean updatePublishedResult(PipelineRecord pipelineRecord, Collection<ReleaseArtifact> releaseArtifacts);

    List<ArtifactInfo> getAllByPipelineRecord(PipelineRecord pipelineRecord);

    List<ArtifactInfo> getByCondition(Long pipelineBuildId, boolean published);

}