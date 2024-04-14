package com.ezone.devops.plugins.job.release.beans;

import com.ezone.devops.plugins.enums.ArtifactType;
import com.ezone.devops.plugins.enums.ReleaseStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReleaseArtifact {

    // 制品的id
    private Long id;
    // 类型
    private ArtifactType format;
    // 制品库
    private String pkgRepo;
    // 包名
    private String pkgName;
    // 临时版本号
    private String snapshotVersion;
    // 发布版本号
    private String releaseVersion;
    // 是否发布
    private boolean published;
    // 发版状态
    private ReleaseStatus releaseStatus;
    // 发版结果
    private String releaseResult;

}