package com.ezone.devops.pipeline.service;

import com.ezone.devops.pipeline.model.ReleaseVersion;
import com.ezone.devops.pipeline.vo.RepoVo;

public interface VersionService {

    String initVersion(RepoVo repoVo);

    String getNextSnapshotVersion(RepoVo repo);

    ReleaseVersion getLastReleaseVersion(RepoVo repo);

    ReleaseVersion getReleaseVersion(RepoVo repo, String version);

    boolean publishReleaseVersion(RepoVo repo, String version, String message);

    void deleteByRepo(RepoVo repo);
}
