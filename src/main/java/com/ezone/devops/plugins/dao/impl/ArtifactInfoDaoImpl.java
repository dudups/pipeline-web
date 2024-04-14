package com.ezone.devops.plugins.dao.impl;

import com.ezone.devops.plugins.dao.ArtifactInfoDao;
import com.ezone.devops.plugins.model.ArtifactInfo;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class ArtifactInfoDaoImpl extends BaseCommonDao<ArtifactInfo> implements ArtifactInfoDao {

    @Override
    public List<ArtifactInfo> findByPipelineBuildId(Long pipelineBuildId) {
        return find(match(PIPELINE_BUILD_ID, pipelineBuildId));
    }

    @Override
    public List<ArtifactInfo> findByPipelineBuildId(Long pipelineBuildId, Collection<Long> ids) {
        return find(match(PIPELINE_BUILD_ID, pipelineBuildId), match(ID, ids));
    }

    @Override
    public List<ArtifactInfo> findByPipelineBuildIdAndPublished(Long pipelineBuildId, boolean published) {
        return find(match(PIPELINE_BUILD_ID, pipelineBuildId), match(PUBLISHED, published));
    }
}
