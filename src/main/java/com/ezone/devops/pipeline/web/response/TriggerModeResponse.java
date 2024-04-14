package com.ezone.devops.pipeline.web.response;

import com.ezone.devops.pipeline.common.TriggerMode;
import com.ezone.devops.pipeline.model.PipelineRecord;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TriggerModeResponse {

    private TriggerMode triggerMode;
    private Object content;

    public static TriggerModeResponse convert(PipelineRecord pipelineRecord, Object content) {
        return new TriggerModeResponse().setTriggerMode(pipelineRecord.getTriggerMode()).setContent(content);
    }

}
