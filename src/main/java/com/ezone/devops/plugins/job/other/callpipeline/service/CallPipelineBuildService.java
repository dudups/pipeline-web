package com.ezone.devops.plugins.job.other.callpipeline.service;

import com.ezone.devops.plugins.job.other.callpipeline.model.CallPipelineBuild;

public interface CallPipelineBuildService {

    boolean updateCallPipelineBuild(CallPipelineBuild callPipelineBuild);

    CallPipelineBuild getById(Long id);

    CallPipelineBuild saveCallPipelineBuild(CallPipelineBuild callPipelineBuild);
}
