package com.ezone.devops.pipeline.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.dao.JobDao;
import com.ezone.devops.pipeline.enums.JobConditionType;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.Stage;
import com.ezone.devops.pipeline.service.JobConditionVariableService;
import com.ezone.devops.pipeline.service.JobService;
import com.ezone.devops.pipeline.web.request.ConditionVariablePair;
import com.ezone.devops.pipeline.web.request.GroupJob;
import com.ezone.devops.pipeline.web.request.JobPayload;
import com.ezone.devops.plugins.service.PluginService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobDao jobDao;
    @Autowired
    private PluginService pluginService;
    @Autowired
    private JobConditionVariableService jobConditionVariableService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveJobs(Pipeline pipeline, Stage stage, List<GroupJob> groupJobs) {
        log.info("start save jobs stage:[{}]", stage);
        for (GroupJob groupJob : groupJobs) {
            Long upstreamId = Job.HEAD_JOB_ID;
            for (JobPayload jobPayload : groupJob.getJobs()) {
                Job job = new Job(stage, jobPayload, upstreamId);
                // 保存插件的数据
                Long pluginId = pluginService.savePluginConfig(job, jobPayload.getRealJob());
                job.setPluginId(pluginId);
                jobDao.save(job);

                JobConditionType executeType = jobPayload.getConditionType();
                if (executeType == JobConditionType.VARIABLE_MATCH_ALL || executeType == JobConditionType.VARIABLE_MATCH_ANY || executeType == JobConditionType.VARIABLE_NOT_MATCH) {
                    jobConditionVariableService.saveJobConditionVariables(pipeline, job, jobPayload.getConditionVariable());
                }

                upstreamId = job.getId();
                log.info("save stage jobs, job:[{}]", job);
            }
        }

        log.info("finished save jobs stage:[{}]", stage);
        return true;
    }

    @Override
    public boolean deleteJobs(Pipeline pipeline, List<Stage> stages) {
        if (CollectionUtils.isEmpty(stages)) {
            return false;
        }

        Set<Long> stageIds = stages.stream().map(Stage::getId).collect(Collectors.toSet());
        List<Job> jobs = jobDao.getJobsByStageIds(stageIds);
        jobConditionVariableService.deleteByPipeline(pipeline);
        // 为了支持重新执行，插件的配置不能删除
        return jobDao.delete(jobs) > 0;

    }

    /**
     * 根据stageId获取所有stage下的job
     */
    private List<Job> getJobConfigByStageConfigId(Long stageConfigId) {
        return jobDao.getJobsByStageId(stageConfigId);
    }

    @Override
    public List<List<Job>> getOrderedJobsByStage(Pipeline pipeline, Stage stage) {
        List<Job> jobs = getJobConfigByStageConfigId(stage.getId());
        List<List<Job>> jobGroups = Lists.newArrayList();
        if (CollectionUtils.isEmpty(jobs)) {
            return jobGroups;
        }

        // 存储<upstreamJobStartId, JobConfig>的键值对，用于获取上下游关系
        Map<Long, Job> jobMap = Maps.newHashMap();
        for (Job job : jobs) {
            if (Job.HEAD_JOB_ID.equals(job.getUpstreamId())) {
                jobGroups.add(Lists.newArrayList(Lists.newArrayList(job)));
            } else {
                jobMap.put(job.getUpstreamId(), job);
            }
        }

        // 按照开始job组装每条job线
        for (List<Job> jobGroupList : jobGroups) {
            Job currentJob = jobGroupList.get(0);
            Long upstreamId = currentJob.getId();
            for (int i = 0; i < jobs.size(); i++) {
                Job subJob = jobMap.get(upstreamId);
                // 到达此条线末尾，break
                if (null == subJob) {
                    break;
                }

                upstreamId = subJob.getId();
                jobGroupList.add(subJob);
            }
        }

        return jobGroups;
    }

    @Override
    public List<GroupJob> getOrderedJobPayloads(Pipeline pipeline, Stage stage) {
        List<List<Job>> orderedJobList = getOrderedJobsByStage(pipeline, stage);
        if (CollectionUtils.isEmpty(orderedJobList)) {
            return null;
        }

        List<GroupJob> groupJobs = new ArrayList<>();
        for (List<Job> orderedJobs : orderedJobList) {
            GroupJob groupJob = new GroupJob();
            List<JobPayload> jobs = Lists.newArrayListWithCapacity(orderedJobs.size());
            for (Job job : orderedJobs) {
                JobPayload jobPayload = new JobPayload();
                jobPayload.setJobName(job.getName());
                jobPayload.setJobType(job.getJobType());
                jobPayload.setPluginType(job.getPluginType());
                JSONObject realJob = pluginService.getPluginConfig(job);
                jobPayload.setRealJob(realJob);

                if (job.getConditionType() != JobConditionType.AUTO && job.getConditionType() != JobConditionType.MANUAL) {
                    Set<ConditionVariablePair> jobConditionVariables = jobConditionVariableService.getJobConditionVariables(pipeline, job);
                    jobPayload.setConditionVariable(jobConditionVariables);
                }
                jobPayload.setConditionType(job.getConditionType());
                jobPayload.setConditionTriggerType(job.getConditionTriggerType());

                jobs.add(jobPayload);
            }
            groupJob.setJobs(jobs);
            groupJobs.add(groupJob);
        }

        return groupJobs;
    }
}
