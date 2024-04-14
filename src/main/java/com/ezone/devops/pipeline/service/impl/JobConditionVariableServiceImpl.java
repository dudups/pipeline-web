package com.ezone.devops.pipeline.service.impl;

import com.ezone.devops.pipeline.dao.JobConditionVariableDao;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.pipeline.model.JobConditionVariable;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.service.JobConditionVariableService;
import com.ezone.devops.pipeline.web.request.ConditionVariablePair;
import com.ezone.galaxy.fasterdao.context.ShardingContext;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class JobConditionVariableServiceImpl implements JobConditionVariableService {

    @Autowired
    private JobConditionVariableDao jobConditionVariableDao;

    @Override
    public Set<ConditionVariablePair> getJobConditionVariables(Pipeline pipeline, Job job) {
        ShardingContext.putShardKey(pipeline.getId());
        Set<ConditionVariablePair> result = Sets.newHashSet();
        List<JobConditionVariable> jobConditionVariables = jobConditionVariableDao.getByJobId(job.getId());
        if (CollectionUtils.isEmpty(jobConditionVariables)) {
            return null;
        }

        for (JobConditionVariable jobConditionVariable : jobConditionVariables) {
            ConditionVariablePair variablePair = new ConditionVariablePair();
            variablePair.setEnvKey(jobConditionVariable.getEnvKey());
            variablePair.setEnvValue(jobConditionVariable.getEnvValue());
            result.add(variablePair);
        }

        return result;
    }

    @Override
    public Map<String, String> getJobConditionVariables(Pipeline pipeline, JobRecord jobRecord) {
        ShardingContext.putShardKey(pipeline.getId());
        return getAllVariables(jobRecord.getJobId());
    }

    private Map<String, String> getAllVariables(Long jobId) {
        Map<String, String> variableMap = Maps.newHashMap();
        List<JobConditionVariable> conditionVariables = jobConditionVariableDao.getByJobId(jobId);
        if (CollectionUtils.isEmpty(conditionVariables)) {
            return variableMap;
        }

        for (JobConditionVariable conditionVariable : conditionVariables) {
            String envKey = conditionVariable.getEnvKey();
            String envValue = conditionVariable.getEnvValue();
            if (StringUtils.isBlank(envKey) || StringUtils.isBlank(envValue)) {
                continue;
            }

            variableMap.put(envKey, envValue);
        }

        return variableMap;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveJobConditionVariables(Pipeline pipeline, Job job, Set<ConditionVariablePair> variables) {
        ShardingContext.putShardKey(pipeline.getId());
        List<JobConditionVariable> jobConditionVariables = Lists.newArrayList();
        if (CollectionUtils.isEmpty(variables)) {
            return false;
        }

        for (ConditionVariablePair variable : variables) {
            String envKey = variable.getEnvKey();
            String envValue = variable.getEnvValue();
            if (StringUtils.isBlank(envKey) || StringUtils.isBlank(envValue)) {
                continue;
            }

            JobConditionVariable jobConditionVariable = new JobConditionVariable();
            jobConditionVariable.setPipelineId(pipeline.getId());
            jobConditionVariable.setJobId(job.getId());
            jobConditionVariable.setEnvKey(envKey);
            jobConditionVariable.setEnvValue(envValue);
            jobConditionVariables.add(jobConditionVariable);
        }
        jobConditionVariableDao.save(jobConditionVariables);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteByPipeline(Pipeline pipeline) {
        return jobConditionVariableDao.deleteByPipelineId(pipeline.getId()) > 0;
    }
}
