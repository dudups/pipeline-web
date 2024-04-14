package com.ezone.devops.pipeline.service;

import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.pipeline.web.request.RuntimeVariablePayload;
import com.ezone.devops.pipeline.web.request.VariablePair;

import java.util.Map;
import java.util.Set;

public interface RuntimeVariableService {

    Map<String, String> getAllVariables(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord);

    boolean initRuntimeVariables(Pipeline pipeline, PipelineRecord pipelineRecord, Set<VariablePair> variables);

    boolean mergeRuntimeVariables(Pipeline pipeline, PipelineRecord pipelineRecord, Set<VariablePair> variables);

    boolean deleteRuntimeVariables(PipelineRecord pipelineRecord);

}
