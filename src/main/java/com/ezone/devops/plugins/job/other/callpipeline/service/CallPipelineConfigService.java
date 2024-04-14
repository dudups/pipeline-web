package com.ezone.devops.plugins.job.other.callpipeline.service;

import com.ezone.devops.plugins.job.other.callpipeline.model.CallPipelineConfig;

public interface CallPipelineConfigService {

    boolean deleteCallPipelineConfig(Long id);

    CallPipelineConfig getById(Long id);

    CallPipelineConfig saveCallPipelineConfig(CallPipelineConfig callPipelineConfig);
}
