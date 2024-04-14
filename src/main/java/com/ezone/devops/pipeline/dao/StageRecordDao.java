package com.ezone.devops.pipeline.dao;

import com.ezone.devops.pipeline.model.StageRecord;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

import java.util.Collection;
import java.util.List;

public interface StageRecordDao extends LongKeyBaseDao<StageRecord> {

    String ID = "id";
    String UPSTREAM_ID = "upstream_id";
    String PIPELINE_ID = "pipeline_id";
    String PIPELINE_RECORD_ID = "pipeline_record_id";
    String NAME = "name";
    String STATUS = "status";
    String CREATE_TIME = "create_time";
    String MODIFY_TIME = "modify_time";

    List<StageRecord> getByPipelineBuildId(Long pipelineRecordId);

    List<StageRecord> getByPipelineBuildIds(Collection<Long> pipelineBuildIds);

    StageRecord getFirstStageByPipelineBuildId(Long pipelineBuildId);

    List<StageRecord> getParentStageRecords(Long pipelineRecordId, Long stageBuildId);

    int deleteNotExistByPipelineIds(Collection<Long> pipelineIds);

    void deleteAll();

    void deleteByPipelineId(Long pipelineId);

    void deleteByPipelineRecordId(Long pipelineRecordId);
}
