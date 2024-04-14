package com.ezone.devops.pipeline.dao;

import com.ezone.devops.pipeline.model.BranchPatternConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

import java.util.Collection;
import java.util.List;

public interface BranchPatternConfigDao extends LongKeyBaseDao<BranchPatternConfig> {

    String ID = "id";
    String PIPELINE_ID = "pipeline_id";
    String MATCH_TYPE = "match_type";
    String PATTERN = "pattern";

    List<BranchPatternConfig> getByPipelineIds(Collection<Long> pipelineIds);

    BranchPatternConfig getByPipelineId(Long pipelineId);

    boolean deleteBranchPatternByPipelineId(Long pipelineId);

    void deleteAll();

    int deleteNotExistByPipelineIds(Collection<Long> pipelineIds);
}
