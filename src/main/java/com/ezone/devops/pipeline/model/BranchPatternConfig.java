package com.ezone.devops.pipeline.model;

import com.ezone.devops.pipeline.dao.BranchPatternConfigDao;
import com.ezone.devops.pipeline.enums.BranchMatchType;
import com.ezone.devops.pipeline.web.request.BranchPatternConfigPayload;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "branch_pattern_config")
public class BranchPatternConfig extends LongID {

    @Column(BranchPatternConfigDao.ID)
    private Long id;
    @Column(BranchPatternConfigDao.PIPELINE_ID)
    private Long pipelineId;
    @Column(BranchPatternConfigDao.MATCH_TYPE)
    private BranchMatchType matchType;
    @Column(BranchPatternConfigDao.PATTERN)
    private String pattern;

    public BranchPatternConfig(Pipeline pipeline, BranchPatternConfigPayload branchPatternConfigPayload) {
        setPipelineId(pipeline.getId());
        setMatchType(branchPatternConfigPayload.getMatchType());
        setPattern(branchPatternConfigPayload.getPattern());
    }
}
