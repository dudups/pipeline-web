package com.ezone.devops.pipeline.web.response;

import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.model.StageRecord;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

@Data
@NoArgsConstructor
public class PipelineRecordResponse {

    private PipelineBuildVo pipelineBuild;
    private List<StageBuildVo> stageBuilds;

    public PipelineRecordResponse(PipelineRecord pipelineRecord, List<StageRecord> stageRecords) {
        setPipelineBuild(new PipelineBuildVo(pipelineRecord));
        if (CollectionUtils.isNotEmpty(stageRecords)) {
            List<StageBuildVo> stageBuildVos = Lists.newArrayListWithCapacity(stageRecords.size());
            for (StageRecord stageRecord : stageRecords) {
                stageBuildVos.add(new StageBuildVo(stageRecord));
            }
            setStageBuilds(stageBuildVos);
        }
    }
}
