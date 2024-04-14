package com.ezone.devops.pipeline.web.response;

import com.ezone.devops.pipeline.common.BuildStatus;
import com.ezone.devops.pipeline.common.ScmTriggerType;
import com.ezone.devops.pipeline.common.TriggerMode;
import com.ezone.devops.pipeline.model.PipelineRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PipelineBuildVo {

    private Long id;
    private Long pipelineId;
    private String repoKey;
    private String pipelineName;
    private String snapshotVersion;
    private String releaseVersion;
    private Long buildNumber;
    private ScmTriggerType scmTriggerType;
    private String externalName;
    private String commitId;
    private BuildStatus status;
    private TriggerMode triggerMode;
    private String triggerUser;
    private String externalKey;
    private String dashboardUrl;
    private Date createTime;
    private Date modifyTime;

    public PipelineBuildVo(PipelineRecord pipelineRecord) {
        setId(pipelineRecord.getId());
        setPipelineId(pipelineRecord.getPipelineId());
        setRepoKey(pipelineRecord.getRepoKey());
        setPipelineName(pipelineRecord.getPipelineName());
        setSnapshotVersion(pipelineRecord.getSnapshotVersion());
        setReleaseVersion(pipelineRecord.getReleaseVersion());
        setBuildNumber(pipelineRecord.getBuildNumber());
        setScmTriggerType(pipelineRecord.getScmTriggerType());
        setExternalName(pipelineRecord.getExternalName());
        setCommitId(pipelineRecord.getCommitId());
        setStatus(pipelineRecord.getStatus());
        setTriggerMode(pipelineRecord.getTriggerMode());
        setTriggerUser(pipelineRecord.getTriggerUser());
        setExternalKey(pipelineRecord.getExternalKey());
        setDashboardUrl(pipelineRecord.getDashboardUrl());
        setCreateTime(pipelineRecord.getCreateTime());
        setModifyTime(pipelineRecord.getModifyTime());

    }
}
