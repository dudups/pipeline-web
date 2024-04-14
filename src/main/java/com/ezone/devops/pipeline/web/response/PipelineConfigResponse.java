package com.ezone.devops.pipeline.web.response;

import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.Stage;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

@Data
@NoArgsConstructor
public class PipelineConfigResponse {

    private PipelineVo pipelineConfig;
    private List<StageVo> stageConfigs;

    public PipelineConfigResponse(Pipeline pipeline) {
        this(pipeline, null);
    }

    public PipelineConfigResponse(Pipeline pipeline, List<Stage> stages) {
        setPipelineConfig(new PipelineVo(pipeline));
        if (CollectionUtils.isNotEmpty(stages)) {
            List<StageVo> stageConfigVos = Lists.newArrayListWithCapacity(stages.size());
            for (Stage stage : stages) {
                stageConfigVos.add(new StageVo(stage));
            }
            setStageConfigs(stageConfigVos);
        }
    }
}
