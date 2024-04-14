package com.ezone.devops.pipeline.model;

import com.ezone.devops.pipeline.common.TriggerMode;
import com.ezone.devops.pipeline.dao.PipelineTriggerConfigDao;
import com.ezone.devops.pipeline.web.request.TriggerConfigPayload;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import com.ezone.galaxy.framework.common.util.JsonUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * 流水线定时任务配置表.
 */
@Data
@Table(name = "pipeline_trigger_config")
@NoArgsConstructor
public class PipelineTriggerConfig extends LongID {

    @Column(PipelineTriggerConfigDao.ID)
    private Long id;
    @Column(PipelineTriggerConfigDao.PIPELINE_ID)
    private Long pipelineId;
    @Column(PipelineTriggerConfigDao.TRIGGER_MODE)
    private TriggerMode triggerMode;

    @Column(PipelineTriggerConfigDao.CI_EVENT)
    private String ciEvent;
    @Column(PipelineTriggerConfigDao.CRONTAB)
    private String crontab;
    @Column(PipelineTriggerConfigDao.POLL_SCM)
    private boolean pollScm;

    public PipelineTriggerConfig(Pipeline pipeline, TriggerConfigPayload payload) {
        setPipelineId(pipeline.getId());
        setTriggerMode(payload.getTriggerMode());
        setCiEvent(StringUtils.defaultIfEmpty(JsonUtils.toJson(payload.getCiEvents()), StringUtils.EMPTY));
        setCrontab(StringUtils.defaultIfEmpty(JsonUtils.toJson(payload.getCrontab()), StringUtils.EMPTY));
        setPollScm(payload.isPollScm());
    }
}
