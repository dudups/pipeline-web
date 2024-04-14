package com.ezone.devops.pipeline.model;

import com.ezone.devops.pipeline.common.BuildStatus;
import com.ezone.devops.pipeline.common.ScmTriggerType;
import com.ezone.devops.pipeline.common.TriggerMode;
import com.ezone.devops.pipeline.dao.PipelineRecordDao;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@Data
@NoArgsConstructor
@Table(name = "pipeline_record")
public class PipelineRecord extends LongID {

    @Column(PipelineRecordDao.ID)
    private Long id;
    @Column(PipelineRecordDao.COMPANY_ID)
    private Long companyId;
    @Column(PipelineRecordDao.PIPELINE_ID)
    private Long pipelineId;
    @Column(PipelineRecordDao.PIPELINE_NAME)
    private String pipelineName;
    @Column(PipelineRecordDao.JOB_TIMEOUT_MINUTE)
    private int jobTimeoutMinute;
    @Column(PipelineRecordDao.SNAPSHOT_VERSION)
    private String snapshotVersion;
    @Column(PipelineRecordDao.RELEASE_VERSION)
    private String releaseVersion;
    @Column(PipelineRecordDao.BUILD_NUMBER)
    private Long buildNumber;

    @Column(PipelineRecordDao.REPO_KEY)
    private String repoKey;

    @Column(PipelineRecordDao.SCM_TRIGGER_TYPE)
    private ScmTriggerType scmTriggerType;
    @Column(PipelineRecordDao.EXTERNAL_NAME)
    private String externalName;
    @Column(PipelineRecordDao.COMMIT_ID)
    private String commitId;
    @Column(PipelineRecordDao.STATUS)
    private BuildStatus status;
    @Column(PipelineRecordDao.TRIGGER_MODE)
    private TriggerMode triggerMode;
    @Column(PipelineRecordDao.TRIGGER_USER)
    private String triggerUser;
    @Column(PipelineRecordDao.EXTERNAL_KEY)
    private String externalKey;
    @Column(PipelineRecordDao.DASHBOARD_URL)
    private String dashboardUrl;
    @Column(PipelineRecordDao.CALLBACK_URL)
    private String callbackUrl;
    @Column(value = PipelineRecordDao.CREATE_TIME)
    private Date createTime;
    @Column(PipelineRecordDao.MODIFY_TIME)
    private Date modifyTime;

    public PipelineRecord(Pipeline pipeline, RepoVo repo, ScmTriggerType scmTriggerType, String externalName,
                          String commitId, String user, TriggerMode triggerMode, Long buildNumber,
                          String snapshotVersion, String externalKey, String dashboardUrl, String callbackUrl) {
        setCompanyId(pipeline.getCompanyId());
        setPipelineId(pipeline.getId());
        setPipelineName(pipeline.getName());
        setRepoKey(repo.getRepoKey());

        setScmTriggerType(scmTriggerType);
        setExternalName(StringUtils.defaultIfBlank(externalName, StringUtils.EMPTY));

        setCommitId(commitId);
        setJobTimeoutMinute(pipeline.getJobTimeoutMinute());

        setTriggerMode(triggerMode);
        setTriggerUser(user);

        setStatus(BuildStatus.WAITING);

        setBuildNumber(buildNumber);
        setSnapshotVersion(snapshotVersion);
        setReleaseVersion(StringUtils.EMPTY);

        setExternalKey(StringUtils.defaultIfBlank(externalKey, StringUtils.EMPTY));
        setDashboardUrl(StringUtils.defaultIfBlank(dashboardUrl, StringUtils.EMPTY));
        setCallbackUrl(StringUtils.defaultIfBlank(callbackUrl, StringUtils.EMPTY));

        setCreateTime(new Date());
    }
}
