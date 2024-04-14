package com.ezone.devops.pipeline.dao.impl;

import com.ezone.devops.pipeline.dao.PipelinePermissionDao;
import com.ezone.devops.pipeline.enums.PipelinePermissionType;
import com.ezone.devops.pipeline.enums.UserType;
import com.ezone.devops.pipeline.model.PipelinePermission;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PipelinePermissionDaoImpl extends BaseCommonDao<PipelinePermission> implements PipelinePermissionDao {

    @Override
    public List<PipelinePermission> findByPipelineId(Long pipelineId) {
        return find(match(PIPELINE_ID, pipelineId));
    }

    @Override
    public List<PipelinePermission> findByPipelineIdAndPermissionType(Long pipelineId, PipelinePermissionType pipelinePermissionType) {
        return find(match(PIPELINE_ID, pipelineId), match(PERMISSION_TYPE, pipelinePermissionType));
    }

    @Override
    public List<PipelinePermission> findByRepoKeyAndUsername(String repoKey, String username) {
        return find(match(REPO_KEY, repoKey), match(NAME, username), match(TYPE, UserType.USER));
    }

    @Override
    public List<PipelinePermission> findByRepoKeyAndPipelineIdAndUsername(String repoKey, Long pipelineId, String username) {
        return find(match(REPO_KEY, repoKey), match(PIPELINE_ID, pipelineId), match(NAME, username), match(TYPE, UserType.USER));
    }

    @Override
    public List<PipelinePermission> findByRepoKeyAndGroups(String repoKey, List<String> groupNames) {
        return find(match(REPO_KEY, repoKey), match(NAME, groupNames), match(TYPE, UserType.GROUP));
    }

    @Override
    public List<PipelinePermission> findByRepoKeyAndPipelineIdAndGroups(String repoKey, Long pipelineId, List<String> groupNames) {
        return find(match(REPO_KEY, repoKey), match(PIPELINE_ID, pipelineId), match(NAME, groupNames), match(TYPE, UserType.GROUP));
    }

    @Override
    public boolean deleteByPipelineId(Long pipelineId) {
        return delete(match(PIPELINE_ID, pipelineId)) > 0;
    }
}
