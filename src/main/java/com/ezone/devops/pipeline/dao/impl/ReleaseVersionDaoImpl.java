package com.ezone.devops.pipeline.dao.impl;

import com.ezone.devops.pipeline.dao.ReleaseVersionDao;
import com.ezone.devops.pipeline.model.ReleaseVersion;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import com.ezone.galaxy.fasterdao.param.NotParam;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Repository
public class ReleaseVersionDaoImpl extends BaseCommonDao<ReleaseVersion> implements ReleaseVersionDao {

    @Override
    public ReleaseVersion getLastVersionByRepoKey(String repoKey) {
        List<ReleaseVersion> releaseVersions = find(Arrays.asList(match(REPO_KEY, repoKey)),
                Arrays.asList(order(ID, false)), 0, 1);
        if (CollectionUtils.isNotEmpty(releaseVersions)) {
            return releaseVersions.get(0);
        }
        return null;
    }

    @Override
    public ReleaseVersion getByRepoIdAndVersion(String repoKey, String version) {
        return findOne(match(REPO_KEY, repoKey), match(VERSION, version));
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
