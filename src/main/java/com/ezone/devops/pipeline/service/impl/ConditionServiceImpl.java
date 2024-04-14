package com.ezone.devops.pipeline.service.impl;

import com.ezone.devops.pipeline.common.BlockInfo;
import com.ezone.devops.pipeline.condition.job.JobExecuteCondition;
import com.ezone.devops.pipeline.enums.JobConditionType;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.service.ConditionService;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.galaxy.framework.common.bean.BaseException;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ConditionServiceImpl implements ConditionService {

    private final Map<JobConditionType, JobExecuteCondition> CONDITIONS = Maps.newHashMap();

    @Autowired
    public void initConditions(List<JobExecuteCondition> jobExecuteConditions) {
        for (JobExecuteCondition jobExecuteCondition : jobExecuteConditions) {
            JobConditionType jobExecuteType = jobExecuteCondition.getJobExecuteType();
            if (CONDITIONS.containsKey(jobExecuteType)) {
                throw new BaseException(HttpStatus.CONFLICT.value(), "job condition [" + jobExecuteType + "] already exist");
            }
            CONDITIONS.put(jobExecuteType, jobExecuteCondition);
        }
    }

    public BlockInfo blockJobExecute(RepoVo repoVo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        JobConditionType executeType = jobRecord.getConditionType();
        JobExecuteCondition jobExecuteCondition = CONDITIONS.get(executeType);
        if (jobExecuteCondition == null) {
            return new BlockInfo();
        }
        return jobExecuteCondition.execute(repoVo, pipeline, pipelineRecord, jobRecord);
    }

}
