package com.ezone.devops.pipeline.dao;

import com.ezone.devops.pipeline.common.TriggerMode;
import com.ezone.devops.pipeline.model.PipelineTriggerConfig;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

import java.util.List;

public interface PipelineTriggerConfigDao extends LongKeyBaseDao<PipelineTriggerConfig> {

    String ID = "id";
    String PIPELINE_ID = "pipeline_id";
    String TRIGGER_MODE = "trigger_mode";
    String CI_EVENT = "ci_event";
    String CRONTAB = "crontab";
    String POLL_SCM = "poll_scm";

    PipelineTriggerConfig getByPipelineId(Long pipelineId);

    List<PipelineTriggerConfig> getByTriggerMode(TriggerMode triggerMode);
}
