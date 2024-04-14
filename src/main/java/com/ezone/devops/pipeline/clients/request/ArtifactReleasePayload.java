package com.ezone.devops.pipeline.clients.request;

import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.model.ArtifactInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ArtifactReleasePayload {

    private Long companyId;
    private Long pipelineId;
    private Long jobId;
    private String releaseVersion;
    private String username;
    private List<ArtifactInfo> releaseArtifacts;

    public ArtifactReleasePayload(RepoVo repo, JobRecord jobRecord, String releaseVersion, List<ArtifactInfo> artifactInfos) {
        setCompanyId(repo.getCompanyId());
        setPipelineId(jobRecord.getPipelineId());
        setJobId(jobRecord.getExternalJobId());

        setReleaseVersion(releaseVersion);
        setUsername(jobRecord.getTriggerUser());
        setReleaseArtifacts(artifactInfos);
    }
}
