package com.ezone.devops.pipeline.service;

import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.Variable;
import com.ezone.devops.pipeline.web.request.EnvConfigPayload;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface VariableService {

    void saveVariable(Pipeline pipeline, Collection<EnvConfigPayload> payloads);

    void updateVariable(Pipeline pipeline, Collection<EnvConfigPayload> payloads);

    Map<String, String> getEnvsByPipelineId(Long pipelineId);

    List<Variable> getByPipelineId(Long pipelineId);

    List<Variable> getAllVariable(Pipeline pipeline);

    List<Variable> filterSecretVariable(Pipeline pipeline);

    List<EnvConfigPayload> filterSecretVariablePayload(Pipeline pipeline);

}
