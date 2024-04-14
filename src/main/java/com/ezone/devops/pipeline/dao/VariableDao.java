package com.ezone.devops.pipeline.dao;

import com.ezone.devops.pipeline.model.Variable;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface VariableDao extends LongKeyBaseDao<Variable> {

    String ID = "id";
    String PIPELINE_ID = "pipeline_id";
    String ENV_KEY = "env_key";
    String ENV_VALUE = "env_value";
    String SECRET = "secret";
    String DESCRIPTION = "description";

    Map<String, Variable> getMapByPipelineId(Long pipelineId);

    List<Variable> getByPipelineId(Long pipelineId);

    void deleteAll();

    int deleteNotExistByPipelineIds(Collection<Long> pipelineIds);
}
