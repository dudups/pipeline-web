package com.ezone.devops.pipeline.service;

import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.Stage;
import com.ezone.devops.pipeline.web.request.StagePayload;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface StageService {

    void saveStage(Pipeline pipeline, List<StagePayload> stagePayloads);

    void updateStage(Pipeline pipeline, List<StagePayload> stagePayloads);

    boolean deleteStage(Pipeline pipeline);

    Map<Long, List<Stage>> getStageGroup(Collection<Long> pipelineIds);

    List<Stage> getStages(Long pipelineId);

    List<Stage> getOrderedStages(Pipeline pipeline);

    List<StagePayload> getStagePayloads(Pipeline pipeline);

}
