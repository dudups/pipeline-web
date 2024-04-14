package com.ezone.devops.pipeline.service;

import com.ezone.devops.pipeline.enums.PipelinePermissionType;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.pipeline.web.request.PipelinePermissionPayload;

import java.util.Set;
import java.util.TreeSet;

public interface PipelinePermissionService {

    PipelinePermissionPayload getDetails(Pipeline pipeline, String username);

    TreeSet<Long> getAuthorizedPipeline(RepoVo repoVo, String username);

    boolean updatePermission(Pipeline pipeline, PipelinePermissionPayload payload);

    boolean hasPermission(RepoVo repoVo, Pipeline pipeline, PipelinePermissionType pipelinePermissionType, String username);

}