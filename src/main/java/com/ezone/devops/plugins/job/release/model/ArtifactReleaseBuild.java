package com.ezone.devops.plugins.job.release.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.plugins.job.release.dao.ArtifactReleaseBuildDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@Table(name = "plugin_artifact_release_build")
public class ArtifactReleaseBuild extends LongID {

    @JSONField(serialize = false)
    @Column(ArtifactReleaseBuildDao.ID)
    private Long id;
    @JSONField(serialize = false)
    @Column(ArtifactReleaseBuildDao.REPO_KEY)
    private String repoKey;
    @JSONField(serialize = false)
    @Column(ArtifactReleaseBuildDao.PIPELINE_BUILD_ID)
    private Long pipelineBuildId;
    @Column(ArtifactReleaseBuildDao.PUBLISH)
    private boolean publish = false;

    @Column(ArtifactReleaseBuildDao.VERSION)
    private String version;
    @Column(ArtifactReleaseBuildDao.PUSH_TAG)
    private boolean pushTag = true;
    @Column(ArtifactReleaseBuildDao.MESSAGE)
    private String message;
    @Column(ArtifactReleaseBuildDao.SQL_SCRIPT)
    private String sqlScript;

    @Column(ArtifactReleaseBuildDao.CARD_KEYS)
    private String cardKeys;

    public ArtifactReleaseBuild(PipelineRecord pipelineRecord) {
        setRepoKey(pipelineRecord.getReleaseVersion());
        setPipelineBuildId(pipelineRecord.getId());
        setMessage(StringUtils.EMPTY);
        setSqlScript(StringUtils.EMPTY);
        setPublish(false);
        setPushTag(true);
        String releaseVersion = pipelineRecord.getReleaseVersion();
        if (StringUtils.isNotBlank(releaseVersion)) {
            setVersion(releaseVersion);
            setPublish(true);
            return;
        }
        setVersion(StringUtils.EMPTY);
        setCardKeys(StringUtils.EMPTY);
    }
}