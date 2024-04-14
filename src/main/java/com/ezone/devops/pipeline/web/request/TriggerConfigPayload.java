package com.ezone.devops.pipeline.web.request;

import com.ezone.devops.pipeline.common.TriggerMode;
import com.ezone.devops.pipeline.model.PipelineTriggerConfig;
import com.ezone.galaxy.framework.common.util.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@NoArgsConstructor
public class TriggerConfigPayload {

    @NotNull(message = "触发类型不允许为空")
    private TriggerMode triggerMode;
    private Set<TriggerMode> ciEvents;

    private CrontabPayload crontab;
    // 定时触发是否检测代码变更,true: 代码变更触发. 默认false
    private boolean pollScm;

    public TriggerConfigPayload(PipelineTriggerConfig pipelineTriggerConfig) {
        setTriggerMode(pipelineTriggerConfig.getTriggerMode());
        setPollScm(pipelineTriggerConfig.isPollScm());

        setCiEvents(JsonUtils.toObject(pipelineTriggerConfig.getCiEvent(), new TypeReference<Set<TriggerMode>>() {
        }));
        if (pipelineTriggerConfig.getTriggerMode() == TriggerMode.CRONTAB) {
            setCrontab(JsonUtils.toObject(pipelineTriggerConfig.getCrontab(), CrontabPayload.class));
        }
    }
}
