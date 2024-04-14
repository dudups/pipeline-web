package com.ezone.devops.pipeline.service;

import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineTriggerConfig;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.pipeline.web.request.TriggerConfigPayload;

public interface PipelineTriggerService {

    PipelineTriggerConfig createPipelineTriggerConfig(Pipeline pipeline, RepoVo repo,
                                                      TriggerConfigPayload triggerConfigPayload);

    PipelineTriggerConfig updatePipelineTriggerConfig(Pipeline pipeline,
                                                      RepoVo repo,
                                                      TriggerConfigPayload triggerConfigPayload);

    TriggerConfigPayload getTriggerConfigPayload(Pipeline pipeline);

    void deletePipelineCrontabConfig(Pipeline pipeline, RepoVo repo);

}
