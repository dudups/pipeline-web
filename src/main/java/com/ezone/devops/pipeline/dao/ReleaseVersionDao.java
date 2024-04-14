package com.ezone.devops.pipeline.dao;

import com.ezone.devops.pipeline.model.ReleaseVersion;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

import java.util.Collection;

public interface ReleaseVersionDao extends LongKeyBaseDao<ReleaseVersion> {

    String ID = "id";
    String REPO_KEY = "repo_key";
    String VERSION = "version";
    String MESSAGE = "message";

    ReleaseVersion getLastVersionByRepoKey(String repoKey);

    ReleaseVersion getByRepoIdAndVersion(String repoKey, String version);

    int deleteNotExistByRepoKeys(Collection<String> repoKeys);

    void deleteAll();

    void deleteByRepoKey(String repoKey);
}
