package com.ezone.devops.pipeline.dao.impl;

import com.ezone.devops.pipeline.common.TriggerMode;
import com.ezone.devops.pipeline.dao.PipelineTriggerConfigDao;
import com.ezone.devops.pipeline.model.PipelineTriggerConfig;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PipelineTriggerConfigDaoImpl extends BaseCommonDao<PipelineTriggerConfig> implements
        PipelineTriggerConfigDao {

    @Override
    public PipelineTriggerConfig getByPipelineId(Long pipelineId) {
        return findOne(match(PIPELINE_ID, pipelineId));
    }

    @Override
    public List<PipelineTriggerConfig> getByTriggerMode(TriggerMode triggerMode) {
        return find(match(TRIGGER_MODE, triggerMode));
    }

}
