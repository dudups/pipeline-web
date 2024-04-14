package com.ezone.devops.pipeline.dao.impl;

import com.ezone.devops.pipeline.dao.VariableDao;
import com.ezone.devops.pipeline.model.Variable;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import com.ezone.galaxy.fasterdao.param.NotParam;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class VariableDaoImpl extends BaseCommonDao<Variable> implements VariableDao {

    @Override
    public Map<String, Variable> getMapByPipelineId(Long pipelineId) {
        List<Variable> envConfigs = find(match(PIPELINE_ID, pipelineId));
        if (CollectionUtils.isEmpty(envConfigs)) {
            return null;
        }

        return envConfigs.stream().collect(Collectors.toMap(Variable::getEnvKey, envConfig -> envConfig));
    }

    @Override
    public List<Variable> getByPipelineId(Long pipelineId) {
        return find(match(PIPELINE_ID, pipelineId));
    }

    @Override
    public void deleteAll() {
        delete();
    }

    @Override
    public int deleteNotExistByPipelineIds(Collection<Long> pipelineIds) {
        return delete(match(PIPELINE_ID, new NotParam(pipelineIds)));
    }
}
