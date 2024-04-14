package com.ezone.devops.pipeline.service;

import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.web.request.ConditionVariablePair;

import java.util.Map;
import java.util.Set;

public interface JobConditionVariableService {

    Set<ConditionVariablePair> getJobConditionVariables(Pipeline pipeline, Job job);

    Map<String, String> getJobConditionVariables(Pipeline pipeline, JobRecord jobRecord);

    boolean saveJobConditionVariables(Pipeline pipeline, Job job, Set<ConditionVariablePair> variables);

    boolean deleteByPipeline(Pipeline pipeline);

}
