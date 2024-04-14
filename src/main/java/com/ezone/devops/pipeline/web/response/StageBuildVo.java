package com.ezone.devops.pipeline.web.response;

import com.ezone.devops.pipeline.common.BuildStatus;
import com.ezone.devops.pipeline.model.StageRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StageBuildVo {

    private Long id;
    private Long pipelineId;
    private String stageName;
    private BuildStatus status;
    private Date createTime;
    private Date modifyTime;

    public StageBuildVo(StageRecord stageRecord) {
        setId(stageRecord.getId());
        setPipelineId(stageRecord.getPipelineId());
        setStageName(stageRecord.getName());
        setStatus(stageRecord.getStatus());
        setCreateTime(stageRecord.getCreateTime());
        setModifyTime(stageRecord.getModifyTime());
    }
}
