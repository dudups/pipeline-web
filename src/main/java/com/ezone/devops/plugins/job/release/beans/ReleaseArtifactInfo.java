package com.ezone.devops.plugins.job.release.beans;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class ReleaseArtifactInfo {

    // 发布版本号
    private String releaseVersion;
    // 发布说明
    private String message;
    // sql
    private String sqlScript;
    // 是否打标签
    private boolean pushTag = true;
    // 制品列表
    private List<ReleaseArtifact> releaseArtifacts;
    // 发版的卡片
    private Set<String> cardsKeys;

}