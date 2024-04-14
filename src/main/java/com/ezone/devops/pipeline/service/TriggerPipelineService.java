package com.ezone.devops.pipeline.service;

import com.ezone.devops.pipeline.common.TriggerMode;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.mq.bean.TriggerMessage;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.pipeline.web.request.TriggerPipelinePayload;

public interface TriggerPipelineService {

    boolean asyncCrontabTriggerPipeline(Pipeline pipeline);

    PipelineRecord triggerPipeline(Pipeline pipeline, TriggerPipelinePayload payload, String user, TriggerMode mode, String callbackUrl);

    PipelineRecord triggerPipelineOnlyForBranch(Pipeline pipeline, RepoVo repo, TriggerMessage message);

}
