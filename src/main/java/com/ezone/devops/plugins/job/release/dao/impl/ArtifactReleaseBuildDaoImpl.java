package com.ezone.devops.plugins.job.release.dao.impl;

import com.ezone.devops.plugins.job.release.dao.ArtifactReleaseBuildDao;
import com.ezone.devops.plugins.job.release.model.ArtifactReleaseBuild;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import com.ezone.galaxy.fasterdao.operator.Match;
import com.ezone.galaxy.fasterdao.param.NotParam;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Repository
public class ArtifactReleaseBuildDaoImpl extends BaseCommonDao<ArtifactReleaseBuild> implements ArtifactReleaseBuildDao {

    @Override
    public ArtifactReleaseBuild findByPipelineBuildId(Long pipelineBuildId) {
        List<ArtifactReleaseBuild> artifactReleases = find(Arrays.asList(match(PIPELINE_BUILD_ID, pipelineBuildId)),
                Arrays.asList(order(ID, false)), 0, 1);
        return CollectionUtils.isNotEmpty(artifactReleases) ? artifactReleases.get(0) : null;
    }

    @Override
    public ArtifactReleaseBuild hasSameReleasedVersion(String repoKey, String version) {
        List<Match> matches = Lists.newArrayList();
        matches.add(match(PUBLISH, true));
        matches.add(match(REPO_KEY, repoKey));
        matches.add(match(VERSION, version));
        return findOne(matches);
    }

    @Override
    public void deleteAll() {
        delete();
    }

    @Override
    public int deleteNotExistByRepoKeys(Set<String> repoKeys) {
        return delete(match(REPO_KEY, new NotParam(repoKeys)));
    }
}
