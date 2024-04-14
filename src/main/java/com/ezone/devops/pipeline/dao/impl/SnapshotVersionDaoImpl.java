package com.ezone.devops.pipeline.dao.impl;

import com.ezone.devops.pipeline.dao.SnapshotVersionDao;
import com.ezone.devops.pipeline.model.SnapshotVersion;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import com.ezone.galaxy.fasterdao.param.NotParam;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class SnapshotVersionDaoImpl extends BaseCommonDao<SnapshotVersion> implements SnapshotVersionDao {

    @Override
    public SnapshotVersion getSnapshotVersionByRepoKey(String repoKey) {
        return findOne(match(REPO_KEY, repoKey));
    }

    @Override
    public int deleteNotExistByRepoKeys(Collection<String> repoKeys) {
        return delete(match(REPO_KEY, new NotParam(repoKeys)));
    }

    @Override
    public void deleteAll() {
        delete();
    }

    @Override
    public void deleteByRepoKey(String repoKey) {
        delete(match(REPO_KEY, repoKey));
    }
}
