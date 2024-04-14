package com.ezone.devops.plugins.dao;

import com.ezone.devops.plugins.model.ArtifactInfo;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

import java.util.Collection;
import java.util.List;

public interface ArtifactInfoDao extends LongKeyBaseDao<ArtifactInfo> {

    String ID = "id";
    String PIPELINE_BUILD_ID = "pipeline_build_id";
    String FORMAT = "format";
    String PKG_REPO = "pkg_repo";
    String PKG_NAME = "pkg_name";
    String SNAPSHOT_VERSION = "snapshot_version";
    String RELEASE_VERSION = "release_version";
    String PUBLISHED = "published";
    String RELEASE_STATUS = "release_status";
    String RELEASE_RESULT = "release_result";

    List<ArtifactInfo> findByPipelineBuildId(Long pipelineBuildId);

    List<ArtifactInfo> findByPipelineBuildId(Long pipelineBuildId, Collection<Long> ids);

    List<ArtifactInfo> findByPipelineBuildIdAndPublished(Long pipelineBuildId, boolean published);
}
