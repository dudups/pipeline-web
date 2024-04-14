package com.ezone.devops.pipeline.dao;

import com.ezone.devops.pipeline.model.SnapshotVersion;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

import java.util.Collection;

public interface SnapshotVersionDao extends LongKeyBaseDao<SnapshotVersion> {

    String ID = "id";
    String REPO_KEY = "repo_key";
    String VERSION = "version";

    /**
     * 获取当前的临时版本号
     *
     * @param repoKey
     * @return
     */
    SnapshotVersion getSnapshotVersionByRepoKey(String repoKey);

    int deleteNotExistByRepoKeys(Collection<String> repoKeys);

    void deleteAll();

    void deleteByRepoKey(String repoKey);
}
