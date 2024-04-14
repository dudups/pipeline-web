package com.ezone.devops.pipeline.web.request;

import com.ezone.devops.pipeline.common.TriggerMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternalTriggerPipelinePayload extends TriggerPipelinePayload {

    private String triggerUser;
    private TriggerMode triggerMode;
    private String callbackUrl;

}
