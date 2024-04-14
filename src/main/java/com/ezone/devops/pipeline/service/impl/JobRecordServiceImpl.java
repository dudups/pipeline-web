package com.ezone.devops.pipeline.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.common.BuildStatus;
import com.ezone.devops.pipeline.dao.JobRecordDao;
import com.ezone.devops.pipeline.exception.JobRecordNotExistException;
import com.ezone.devops.pipeline.model.*;
import com.ezone.devops.pipeline.mq.bean.WebsocketEvent;
import com.ezone.devops.pipeline.mq.producer.WebsocketProducer;
import com.ezone.devops.pipeline.service.JobRecordService;
import com.ezone.devops.pipeline.service.JobService;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.pipeline.web.response.JobRecordVo;
import com.ezone.devops.plugins.job.PipelineInitContext;
import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.service.PluginService;
import com.ezone.galaxy.framework.common.util.SnowFlakeGenerator;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class JobRecordServiceImpl implements JobRecordService {

    @Autowired
    private JobService jobService;
    @Autowired
    private JobRecordDao jobRecordDao;
    @Autowired
    private PluginService pluginService;
    @Autowired
    private WebsocketProducer websocketProducer;

    @Override
    public JobRecord getByIdIfPresent(Long id) {
        JobRecord jobRecord = jobRecordDao.get(id);
        if (jobRecord == null) {
            throw new JobRecordNotExistException();
        }
        return jobRecord;
    }

    @Override
    public JobRecord getByExternalJobIdIfPresent(Long externalJobId) {
        JobRecord jobBuild = jobRecordDao.getByExternalJobId(externalJobId);
        if (jobBuild == null) {
            throw new JobRecordNotExistException();
        }
        return jobBuild;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void initJobRecords(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, StageRecord stageRecord, Stage stage) {
        List<List<Job>> jobGroups = jobService.getOrderedJobsByStage(pipeline, stage);

        for (List<Job> jobConfigs : jobGroups) {
            Long upstreamId = JobRecord.HEAD_JOB_RECORD_ID;
            for (Job job : jobConfigs) {
                JobRecord jobRecord = new JobRecord(repo, stageRecord, job, upstreamId);
                Long pluginRecordId = initPlugin(repo, pipelineRecord, jobRecord);
                jobRecord.setPluginRecordId(pluginRecordId);
                jobRecordDao.save(jobRecord);
                upstreamId = jobRecord.getId();
            }
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JobRecord reInitJobRecord(RepoVo repo, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        jobRecord.setExternalJobId(SnowFlakeGenerator.getSnowflakeId());
        jobRecord.setCreateTime(null);
        jobRecord.setModifyTime(null);

        jobRecord.setTriggerUser(StringUtils.EMPTY);
        jobRecord.setStatus(BuildStatus.PENDING);

        jobRecord.setLogName(StringUtils.EMPTY);
        jobRecord.setMessage(StringUtils.EMPTY);

        Long pluginRecordId = initPlugin(repo, pipelineRecord, jobRecord);
        jobRecord.setPluginRecordId(pluginRecordId);

        jobRecordDao.update(jobRecord);
        return jobRecord;
    }

    private Long initPlugin(RepoVo repo, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        Plugin plugin = pluginService.getPlugin(jobRecord.getJobType());
        PipelineInitContext context = new PipelineInitContext(repo, pipelineRecord, jobRecord);
        return plugin.getPluginDataOperator().initRealJobRecordByRealJobId(jobRecord.getPluginId(), context);
    }

    @Override
    public JobRecord updateJobRecord(JobRecord jobRecord) {
        jobRecordDao.update(jobRecord);
        JSONObject realJobRecord = pluginService.getPluginBuild(jobRecord);
        JobRecordVo jobBuildVo = new JobRecordVo(jobRecord, realJobRecord);
        websocketProducer.sendEvent(WebsocketEvent.JOBS, jobRecord.getPipelineRecordId(), jobBuildVo);
        return jobRecord;
    }

    @Override
    public List<JobRecord> getJobRecords(StageRecord stageRecord) {
        return jobRecordDao.getJobRecordsByStageRecordId(stageRecord.getId());
    }

    @Override
    public Map<Long, List<List<JobRecordVo>>> getJobRecordGroups(Pipeline pipeline, PipelineRecord pipelineRecord) {
        List<JobRecord> jobRecords = jobRecordDao.getByPipelineAndPipelineRecordId(pipeline.getId(), pipelineRecord.getId());
        if (CollectionUtils.isEmpty(jobRecords)) {
            return null;
        }

        Map<Long, List<List<JobRecord>>> groups = Maps.newHashMap();
        Map<Long, List<JobRecord>> stageGroups = groupByStage(jobRecords);
        for (Map.Entry<Long, List<JobRecord>> entry : stageGroups.entrySet()) {
            List<List<JobRecord>> jobBuildGroup = getJobRecordGroup(entry.getValue());
            groups.put(entry.getKey(), jobBuildGroup);
        }

        Map<Long, List<List<JobRecordVo>>> result = Maps.newHashMap();
        for (Map.Entry<Long, List<List<JobRecord>>> entry : groups.entrySet()) {
            List<List<JobRecordVo>> jobBuildResponses = Lists.newArrayList();

            for (List<JobRecord> jobBuildGroup : entry.getValue()) {
                List<JobRecordVo> jobGroup = Lists.newArrayListWithCapacity(jobBuildGroup.size());
                for (JobRecord jobBuild : jobBuildGroup) {
                    JSONObject realJobRecord = pluginService.getPluginBuild(jobBuild);
                    jobGroup.add(new JobRecordVo(jobBuild, realJobRecord));
                }
                jobBuildResponses.add(jobGroup);
            }
            result.put(entry.getKey(), jobBuildResponses);
        }
        return result;
    }

    private Map<Long, List<JobRecord>> groupByStage(List<JobRecord> jobBuilds) {
        Map<Long, List<JobRecord>> result = Maps.newHashMap();
        for (JobRecord jobBuild : jobBuilds) {
            Long stageBuildId = jobBuild.getStageRecordId();
            if (result.containsKey(stageBuildId)) {
                result.get(stageBuildId).add(jobBuild);
            } else {
                result.put(stageBuildId, Lists.newArrayList(jobBuild));
            }
        }
        return result;
    }

    private List<List<JobRecord>> getJobRecordGroup(List<JobRecord> jobBuilds) {
        List<List<JobRecord>> jobBuildGroup = Lists.newArrayList();
        if (CollectionUtils.isEmpty(jobBuilds)) {
            return jobBuildGroup;
        }

        // 临时存储<upstreamJobId, JobRecord>的键值对，用于获取上下游关系
        Map<Long, JobRecord> upstreamJobId2JobRecord = Maps.newHashMap();
        // 当JobRecord中的upstreamId为0时，表示为头job
        for (JobRecord jobBuild : jobBuilds) {
            if (JobRecord.HEAD_JOB_RECORD_ID.equals(jobBuild.getUpstreamId())) {
                jobBuildGroup.add(Lists.newArrayList(Lists.newArrayList(jobBuild)));
            } else {
                upstreamJobId2JobRecord.put(jobBuild.getUpstreamId(), jobBuild);
            }
        }

        // 组装各job流水线
        for (List<JobRecord> jobBuildList : jobBuildGroup) {
            JobRecord jobBuild = jobBuildList.get(0);
            while (upstreamJobId2JobRecord.containsKey(jobBuild.getId())) {
                jobBuild = upstreamJobId2JobRecord.get(jobBuild.getId());
                jobBuildList.add(jobBuild);
            }
        }

        return jobBuildGroup;
    }

    @Override
    public JobRecord getRunningJob(JobRecord jobBuild) {
        if (jobBuild == null) {
            return null;
        }

        if (jobBuild.getUpstreamId().equals(JobRecord.HEAD_JOB_RECORD_ID)) {
            return null;
        }

        JobRecord runningJob = jobRecordDao.get(jobBuild.getUpstreamId());
        while (runningJob != null) {
            if (runningJob.getStatus() == BuildStatus.RUNNING) {
                return runningJob;
            }

            if (runningJob.getId().equals(JobRecord.HEAD_JOB_RECORD_ID)) {
                runningJob = null;
                break;
            }
            runningJob = jobRecordDao.get(runningJob.getUpstreamId());

        }

        return runningJob;
    }


    @Override
    public JobRecord getNextWaitingJob(JobRecord jobBuild) {
        if (jobBuild == null) {
            return null;
        }

        JobRecord nextJob = jobRecordDao.getNextJob(jobBuild.getStageRecordId(), jobBuild.getId());
        while (nextJob != null) {
            if (nextJob.getStatus() == BuildStatus.WAITING || nextJob.getStatus() == BuildStatus.PENDING) {
                return nextJob;
            }
            nextJob = jobRecordDao.getNextJob(jobBuild.getStageRecordId(), nextJob.getId());
        }

        return nextJob;
    }

    @Override
    public List<JobRecord> getMiniPipelineHeadJobRecords(StageRecord stageRecord) {
        return jobRecordDao.getMiniPipelineHeadJobRecords(stageRecord.getId(), JobRecord.HEAD_JOB_RECORD_ID);
    }

    @Override
    public List<JobRecord> getMiniPipelineByHeadJob(JobRecord headJobBuild) {
        List<JobRecord> minPipelineJobs = Lists.newArrayList();
        minPipelineJobs.add(headJobBuild);
        JobRecord nextJob = jobRecordDao.getNextJob(headJobBuild.getStageRecordId(), headJobBuild.getId());
        while (nextJob != null) {
            minPipelineJobs.add(nextJob);
            nextJob = jobRecordDao.getNextJob(nextJob.getStageRecordId(), nextJob.getId());
        }

        return minPipelineJobs;
    }

    @Override
    public List<JobRecord> getByStatus(String repoKey, Collection<BuildStatus> buildStatuses, Date start, Date end) {
        return jobRecordDao.getByStatus(repoKey, buildStatuses, start, end);
    }

    @Override
    public void deleteByRepo(RepoVo repo) {
        jobRecordDao.deleteByRepoKey(repo.getRepoKey());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByPipelineRecord(PipelineRecord pipelineRecord) {
        jobRecordDao.deleteByPipelineRecordId(pipelineRecord.getId());
    }
}
