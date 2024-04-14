package com.ezone.devops.pipeline.service;

import com.ezone.devops.pipeline.model.Pipeline;

public interface BuildNumberService {

    Long getNextBuildNumber(Pipeline pipeline);

}
