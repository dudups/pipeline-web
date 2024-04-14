package com.ezone.devops.pipeline.service;

import com.ezone.devops.pipeline.model.PipelineRecord;

public interface PipelineHookService {

    void noticeStart(PipelineRecord pipelineRecord);

    void triggerHook(PipelineRecord pipelineRecord);

}
