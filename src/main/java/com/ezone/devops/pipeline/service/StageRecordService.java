package com.ezone.devops.pipeline.service;

import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.model.StageRecord;
import com.ezone.devops.pipeline.vo.RepoVo;

import java.util.List;

public interface StageRecordService {

    StageRecord getByIdIfPresent(Long id);

    void initStageRecords(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord);

    StageRecord updateStageRecord(StageRecord stageBuild);

    StageRecord getFirstStage(PipelineRecord pipelineRecord);

    List<StageRecord> getOrderedStageRecordByPipelineRecord(PipelineRecord pipelineRecord);

    List<StageRecord> getStageRecordByPipelineRecords(List<PipelineRecord> pipelineRecords);

    List<StageRecord> getParentStageRecords(StageRecord stageBuild);

    StageRecord getNextStageRecord(StageRecord stageBuild);

    void deleteByPipeline(Pipeline pipeline);

    boolean deleteByPipelineRecord(PipelineRecord pipelineRecord);
}
