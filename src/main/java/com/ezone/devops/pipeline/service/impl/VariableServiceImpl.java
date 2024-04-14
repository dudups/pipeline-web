package com.ezone.devops.pipeline.service.impl;

import com.ezone.devops.pipeline.dao.VariableDao;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.Variable;
import com.ezone.devops.pipeline.service.VariableService;
import com.ezone.devops.pipeline.web.request.EnvConfigPayload;
import com.ezone.galaxy.framework.common.bean.BaseException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class VariableServiceImpl implements VariableService {

    @Autowired
    private VariableDao variableDao;

    private static final Pattern BUILD_ENV_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]*$");

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveVariable(Pipeline pipeline, Collection<EnvConfigPayload> payloads) {
        if (CollectionUtils.isEmpty(payloads)) {
            return;
        }

        List<Variable> variables = Lists.newArrayList();
        payloads.forEach(envConfigBean -> variables.add(new Variable(pipeline, envConfigBean)));
        validate(variables);

        variableDao.save(variables);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateVariable(Pipeline pipeline, Collection<EnvConfigPayload> payloads) {

        Map<String, Variable> variableMap = variableDao.getMapByPipelineId(pipeline.getId());

        List<Variable> needSaveEnvConfigs = Lists.newArrayList();
        List<Variable> needUpdateEnvConfigs = Lists.newArrayList();

        if (MapUtils.isNotEmpty(variableMap)) {
            for (EnvConfigPayload envConfigPayload : payloads) {
                String envKey = envConfigPayload.getEnvKey();
                if (variableMap.containsKey(envKey)) {
                    Variable variable = variableMap.get(envKey);
                    variable.setEnvValue(StringUtils.defaultIfBlank(envConfigPayload.getEnvValue(), variable.getEnvValue()));
                    variable.setDescription(StringUtils.defaultIfBlank(envConfigPayload.getDescription(), StringUtils.EMPTY));
                    variable.setSecret(envConfigPayload.isSecret());
                    needUpdateEnvConfigs.add(variable);
                    variableMap.remove(envKey);
                    continue;
                }
                needSaveEnvConfigs.add(new Variable(pipeline, envConfigPayload));
            }

        } else {
            if (CollectionUtils.isNotEmpty(payloads)) {
                for (EnvConfigPayload envConfigPayload : payloads) {
                    needSaveEnvConfigs.add(new Variable(pipeline, envConfigPayload));
                }
            }
        }

        validate(needSaveEnvConfigs);
        validate(needUpdateEnvConfigs);

        if (MapUtils.isNotEmpty(variableMap)) {
            variableDao.delete(variableMap.values());
        }

        if (CollectionUtils.isNotEmpty(needSaveEnvConfigs)) {
            variableDao.save(needSaveEnvConfigs);
        }
        if (CollectionUtils.isNotEmpty(needUpdateEnvConfigs)) {
            variableDao.update(needUpdateEnvConfigs);
        }

    }

    @Override
    public Map<String, String> getEnvsByPipelineId(Long pipelineId) {
        Map<String, String> envs = Maps.newHashMap();
        List<Variable> variables = getByPipelineId(pipelineId);
        if (CollectionUtils.isEmpty(variables)) {
            return envs;
        }

        variables.forEach(envConfig -> envs.put(envConfig.getEnvKey(), envConfig.getEnvValue()));
        return envs;
    }

    @Override
    public List<Variable> getByPipelineId(Long pipelineId) {
        return variableDao.getByPipelineId(pipelineId);
    }

    private void validate(Collection<Variable> variables) {
        for (Variable variable : variables) {
            String key = variable.getEnvKey();
            Matcher matcher = BUILD_ENV_PATTERN.matcher(key);
            if (!matcher.matches()) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "用户自定义变量不能以“SYS_”开头，只支持“^[a-zA-Z][a-zA-Z0-9_]*$”");
            }
        }
    }

    @Override
    public List<Variable> getAllVariable(Pipeline pipeline) {
        return variableDao.getByPipelineId(pipeline.getId());
    }

    @Override
    public List<Variable> filterSecretVariable(Pipeline pipeline) {
        List<Variable> allVariable = getAllVariable(pipeline);
        if (CollectionUtils.isEmpty(allVariable)) {
            return null;
        }

        allVariable.forEach(envConfig -> {
            if (envConfig.isSecret()) {
                envConfig.setEnvValue(null);
            }
        });

        return allVariable;
    }

    @Override
    public List<EnvConfigPayload> filterSecretVariablePayload(Pipeline pipeline) {
        List<Variable> variables = filterSecretVariable(pipeline);
        if (CollectionUtils.isEmpty(variables)) {
            return null;
        }
        List<EnvConfigPayload> envConfigsBean = Lists.newArrayListWithCapacity(variables.size());
        for (Variable variable : variables) {
            EnvConfigPayload envConfigPayload = new EnvConfigPayload();
            BeanUtils.copyProperties(variable, envConfigPayload);
            envConfigsBean.add(envConfigPayload);
        }
        return envConfigsBean;
    }

}
