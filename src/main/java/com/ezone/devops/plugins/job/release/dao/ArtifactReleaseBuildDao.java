package com.ezone.devops.plugins.job.release.dao;

import com.ezone.devops.plugins.job.release.model.ArtifactReleaseBuild;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

import java.util.Set;

public interface ArtifactReleaseBuildDao extends LongKeyBaseDao<ArtifactReleaseBuild> {

    String ID = "id";
    String REPO_KEY = "repo_key";
    String PIPELINE_BUILD_ID = "pipeline_build_id";
    String PUBLISH = "publish";
    String VERSION = "version";
    String PUSH_TAG = "push_tag";
    String MESSAGE = "message";
    String SQL_SCRIPT = "sql_script";
    String CARD_KEYS = "card_keys";

    ArtifactReleaseBuild findByPipelineBuildId(Long pipelineBuildId);

    ArtifactReleaseBuild hasSameReleasedVersion(String repoKey, String version);

    void deleteAll();

    int deleteNotExistByRepoKeys(Set<String> repoKeys);
}
