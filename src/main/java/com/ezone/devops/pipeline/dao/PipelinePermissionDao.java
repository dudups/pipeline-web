package com.ezone.devops.pipeline.dao;

import com.ezone.devops.pipeline.enums.PipelinePermissionType;
import com.ezone.devops.pipeline.model.PipelinePermission;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

import java.util.List;

public interface PipelinePermissionDao extends LongKeyBaseDao<PipelinePermission> {

    String ID = "id";
    String COMPANY_ID = "company_id";
    String REPO_KEY = "repo_key";
    String PIPELINE_ID = "pipeline_id";
    String PERMISSION_TYPE = "permission_type";
    String NAME = "name";
    String TYPE = "type";

    List<PipelinePermission> findByPipelineId(Long pipelineId);

    List<PipelinePermission> findByPipelineIdAndPermissionType(Long pipelineId, PipelinePermissionType pipelinePermissionType);

    List<PipelinePermission> findByRepoKeyAndUsername(String repoKey, String username);

    List<PipelinePermission> findByRepoKeyAndPipelineIdAndUsername(String repoKey, Long pipelineId, String username);

    List<PipelinePermission> findByRepoKeyAndGroups(String repoKey, List<String> groupNames);

    List<PipelinePermission> findByRepoKeyAndPipelineIdAndGroups(String repoKey, Long pipelineId, List<String> groupNames);

    boolean deleteByPipelineId(Long pipelineId);
}
