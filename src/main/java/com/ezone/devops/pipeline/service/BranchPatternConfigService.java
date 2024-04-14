package com.ezone.devops.pipeline.service;

import com.ezone.devops.ezcode.sdk.bean.model.InternalBranch;
import com.ezone.devops.pipeline.model.BranchPatternConfig;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.pipeline.web.request.BranchPatternConfigPayload;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface BranchPatternConfigService {

    boolean saveBranchPatternConfig(Pipeline pipeline, List<BranchPatternConfigPayload> payloads);

    boolean updateBranchPatternConfig(Pipeline pipeline, List<BranchPatternConfigPayload> payloads);

    List<BranchPatternConfig> getBranchPattern(Pipeline pipeline);

    List<BranchPatternConfigPayload> getBranchPatternPayload(Pipeline pipeline);

    List<BranchPatternConfig> getByPipelineIds(Collection<Long> pipelineIds);

    List<InternalBranch> getMatchedBranches(RepoVo repo, Pipeline pipeline);

    Set<Pipeline> getMatchedPipelines(RepoVo repo, String branchName);

}
