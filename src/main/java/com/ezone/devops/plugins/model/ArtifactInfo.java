package com.ezone.devops.plugins.model;

import com.ezone.devops.plugins.dao.ArtifactInfoDao;
import com.ezone.devops.plugins.enums.ArtifactType;
import com.ezone.devops.plugins.enums.ReleaseStatus;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "artifact_info")
@NoArgsConstructor
@AllArgsConstructor
public class ArtifactInfo extends LongID {

    @Column(ArtifactInfoDao.ID)
    private Long id;
    @JsonIgnore
    @Column(ArtifactInfoDao.PIPELINE_BUILD_ID)
    private Long pipelineBuildId;
    @Column(ArtifactInfoDao.FORMAT)
    private ArtifactType format;
    @Column(ArtifactInfoDao.PKG_REPO)
    private String pkgRepo;
    @Column(ArtifactInfoDao.PKG_NAME)
    private String pkgName;
    @Column(ArtifactInfoDao.SNAPSHOT_VERSION)
    private String snapshotVersion;
    @Column(ArtifactInfoDao.RELEASE_VERSION)
    private String releaseVersion;
    @Column(ArtifactInfoDao.PUBLISHED)
    private boolean published;

    @Column(ArtifactInfoDao.RELEASE_STATUS)
    private ReleaseStatus releaseStatus;
    @Column(ArtifactInfoDao.RELEASE_RESULT)
    private String releaseResult;

}