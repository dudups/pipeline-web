package com.ezone.devops.pipeline.event.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.clients.CrontabClient;
import com.ezone.devops.pipeline.common.BlockInfo;
import com.ezone.devops.pipeline.common.BuildStatus;
import com.ezone.devops.pipeline.common.ExecutorTaskPool;
import com.ezone.devops.pipeline.config.SystemConfig;
import com.ezone.devops.pipeline.enums.ConditionTriggerType;
import com.ezone.devops.pipeline.enums.JobConditionType;
import com.ezone.devops.pipeline.enums.OperationType;
import com.ezone.devops.pipeline.event.service.JobRecordEventService;
import com.ezone.devops.pipeline.event.service.StageRecordEventService;
import com.ezone.devops.pipeline.exception.NotSupportOperateException;
import com.ezone.devops.pipeline.exception.OperateNotPermittedException;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.service.*;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginOperator;
import com.ezone.devops.plugins.service.PluginService;
import com.ezone.galaxy.framework.common.bean.BaseException;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 事件驱动执行jobBuild的服务类
 */
@Slf4j
@Service
public class JobRecordEventServiceImpl implements JobRecordEventService {

    private static final String CANCEL_BY_WHO = "此任务已由%s取消";
    private static final String SKIP_BY_WHO = "此任务已由%s跳过，任务跳过后将无法再执行";
    private static final int MESSAGE_MAX_LENGTH = 499;
    private static final String OPERATION_KEY_PREFIX = "job:operation:key:";
    @Autowired
    private SystemConfig systemConfig;
    @Autowired
    private CrontabClient crontabClient;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private JobRecordService jobRecordService;
    @Autowired
    private StageRecordEventService stageRecordEventService;
    @Autowired
    private PipelineService pipelineService;
    @Autowired
    private PipelineRecordService pipelineRecordService;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private PluginService pluginService;
    @Autowired
    private RepoService repoService;
    @Autowired
    private ConditionService conditionService;
    @Autowired
    private ExecutorTaskPool executorTaskPool;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void startJobRecordEvent(PipelineRecord pipelineRecord, JobRecord jobRecord, String triggerUser, JSONObject jobParams, boolean forceStart) {
        if (jobRecord.getStatus() == BuildStatus.PENDING || jobRecord.getStatus() == BuildStatus.WAITING) {
            RepoVo repo = repoService.getByRepoKeyIfPresent(pipelineRecord.getCompanyId(), pipelineRecord.getRepoKey());
            Pipeline pipeline = pipelineService.getByIdIfPresent(pipelineRecord.getPipelineId());

            JobConditionType conditionType = jobRecord.getConditionType();
            if (conditionType == JobConditionType.AUTO || forceStart) {
                log.info("start trigger pipeline record:[{}] job:[{}]", pipelineRecord.getId(), jobRecord.getId());
                executorTaskPool.execute(() -> startJob(repo, pipeline, pipelineRecord, jobRecord, triggerUser, jobParams));
            } else if (conditionType == JobConditionType.MANUAL) {
                jobRecord.setStatus(BuildStatus.WAITING);
                jobRecordService.updateJobRecord(jobRecord);
                log.info("pipeline:[{}] of job:[{}] is manual", pipelineRecord.getId(), jobRecord.getId());
            } else {
                BlockInfo blockInfo = conditionService.blockJobExecute(repo, pipeline, pipelineRecord, jobRecord);
                if (blockInfo.isBlocked()) {
                    ConditionTriggerType conditionTriggerType = jobRecord.getConditionTriggerType();
                    if (conditionTriggerType == ConditionTriggerType.STOP) {
                        jobRecord.setStatus(BuildStatus.WAITING);
                        jobRecord.setMessage(blockInfo.getReason());
                        jobRecordService.updateJobRecord(jobRecord);
                    } else {
                        updateJobRecordEvent(pipelineRecord, jobRecord, BuildStatus.SKIP, true, blockInfo.getReason());
                    }
                } else {
                    executorTaskPool.execute(() -> startJob(repo, pipeline, pipelineRecord, jobRecord, triggerUser, jobParams));
                }
            }
        } else {
            throw new NotSupportOperateException();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancelJobRecordEvent(PipelineRecord pipelineRecord, JobRecord jobRecord, String triggerUser) {
        JobRecord oldJobRecord = new JobRecord();
        BeanUtils.copyProperties(jobRecord, oldJobRecord);
        if (jobRecord.getStatus() != BuildStatus.WAITING) {
            jobRecord.setStatus(BuildStatus.CANCEL);
            jobRecord.setMessage(String.format(CANCEL_BY_WHO, triggerUser));
            jobRecord.setModifyTime(new Date());
            jobRecordService.updateJobRecord(jobRecord);
        }

        executorTaskPool.execute(() -> cancelJob(pipelineRecord, oldJobRecord, triggerUser));
        stageRecordEventService.updateStageRecordEvent(oldJobRecord.getStageRecordId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void skipJobRecordEvent(PipelineRecord pipelineRecord, JobRecord jobRecord, String triggerUser) {
        if (jobRecord.getStatus() == BuildStatus.WAITING || jobRecord.getStatus() == BuildStatus.PENDING) {
            jobRecord.setTriggerUser(triggerUser);
            String message = String.format(SKIP_BY_WHO, triggerUser);
            updateJobRecordEvent(pipelineRecord, jobRecord, BuildStatus.SKIP, false, message);
        } else {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "已经存在运行的任务，无法执行跳过");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void skipAllJobRecordEvent(PipelineRecord pipelineRecord, JobRecord headJobRecord, String triggerUser) {
        List<JobRecord> jobRecords = jobRecordService.getMiniPipelineByHeadJob(headJobRecord);
        if (CollectionUtils.isEmpty(jobRecords)) {
            return;
        }

        Set<BuildStatus> jobStatuses = jobRecords.stream().map(JobRecord::getStatus).collect(Collectors.toSet());
        if (!CollectionUtils.containsAll(Sets.newHashSet(BuildStatus.SKIP, BuildStatus.WAITING, BuildStatus.PENDING), jobStatuses)) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "已经存在运行的任务，无法执行跳过");
        }

        String message = String.format(SKIP_BY_WHO, triggerUser);
        Date date = new Date();
        for (int i = 0; i < jobRecords.size(); i++) {
            JobRecord jobRecord = jobRecords.get(i);
            if (BuildStatus.isEnd(jobRecord.getStatus())) {
                continue;
            }

            if (jobRecords.size() == i + 1) {
                // 最后一个job
                jobRecord.setTriggerUser(triggerUser);
                updateJobRecordEvent(pipelineRecord, jobRecord, BuildStatus.SKIP, false, message);
            } else {
                jobRecord.setStatus(BuildStatus.SKIP);
                jobRecord.setTriggerUser(triggerUser);
                jobRecord.setMessage(message);
                jobRecord.setModifyTime(date);
                jobRecordService.updateJobRecord(jobRecord);
            }
        }
    }

    @Override
    public void updateJobRecordEvent(PipelineRecord pipelineRecord, JobRecord jobRecord, BuildStatus status, boolean appendMessage, String message) {
        if (status == BuildStatus.CANCEL) {
            log.info("job already cancel, jobRecordId:[{}], status:[{}], message:[{}]", jobRecord.getId(), status, message);
            return;
        }

        if (status == jobRecord.getStatus()) {
            return;
        }

        log.info("start update job, jobRecordId:[{}], status:[{}], message:[{}]", jobRecord.getId(), status, message);
        Pipeline pipeline = pipelineService.getByIdIfPresent(jobRecord.getPipelineId());

        String allMessage = jobRecord.getMessage();
        message = StringUtils.defaultString(message, StringUtils.EMPTY);
        if (StringUtils.isBlank(allMessage)) {
            allMessage = message;
        } else {
            if (appendMessage) {
                allMessage = allMessage + "\r" + message;
            } else {
                allMessage = StringUtils.substring(message, 0, MESSAGE_MAX_LENGTH);
            }
        }

        if (StringUtils.length(allMessage) > MESSAGE_MAX_LENGTH) {
            allMessage = StringUtils.substring(message, 0, MESSAGE_MAX_LENGTH);
        }

        jobRecord.setStatus(status);
        jobRecord.setMessage(allMessage);

        // 只有job开始的时间才会更新时间
        if (jobRecord.getCreateTime() == null) {
            jobRecord.setCreateTime(new Date());
        }

        if (BuildStatus.isEnd(status)) {
            jobRecord.setModifyTime(new Date());
        }
        jobRecordService.updateJobRecord(jobRecord);

        if (BuildStatus.isEnd(status)) {
            RepoVo repo = repoService.getByRepoKeyIfPresent(pipeline.getCompanyId(), pipeline.getRepoKey());
            if (BuildStatus.SUCCESS == status) {
                noticeService.asyncNoticeJobSuccess(repo, pipelineRecord, jobRecord);
            } else {
                noticeService.asyncNoticeJobFailed(repo, pipelineRecord, jobRecord);
            }
        }

        stageRecordEventService.updateStageRecordEvent(jobRecord.getStageRecordId());

        // 触发下一个job
        triggerNextWaitingJob(pipelineRecord, jobRecord);

        // 取消定时任务
        if (BuildStatus.isEnd(jobRecord.getStatus())) {
            crontabClient.deleteJobTimeout(jobRecord);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void jobOperator(JobRecord jobRecord, String triggerUser, OperationType operationType, JSONObject jobParams) {
        Long jobRecordId = jobRecord.getId();
        // 检查操作频率
        String key = OPERATION_KEY_PREFIX.concat(String.valueOf(jobRecordId));
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, String.valueOf(jobRecordId), systemConfig.getReExecuteLimit(), TimeUnit.SECONDS);
        if (result != null && !result) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "同一条流水线构建操作太频繁，请间隔2秒后再执行");
        }

        if (operationType == OperationType.UPDATE_PARAM) {
            if (jobParams != null) {
                pluginService.updatePluginBuild(jobRecord, jobParams);
            }
            return;
        }

        if (operationType != OperationType.SKIP_ALL) {
            if (jobRecord.getStatus() == BuildStatus.SKIP) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "任务已经跳过，无法进行其他操作");
            }
        }

        PipelineRecord pipelineRecord = pipelineRecordService.getByIdIfPresent(jobRecord.getPipelineRecordId());
        switch (operationType) {
            case START: {
                startJobRecordEvent(pipelineRecord, jobRecord, triggerUser, jobParams, true);
                break;
            }
            case RESTART: {
                restartJobEvent(pipelineRecord, jobRecord, triggerUser, jobParams);
                break;
            }
            case CANCEL: {
                cancelJobRecordEvent(pipelineRecord, jobRecord, triggerUser);
                break;
            }
            case SKIP_SINGLE: {
                skipJobRecordEvent(pipelineRecord, jobRecord, triggerUser);
                break;
            }
            case SKIP_ALL: {
                skipAllJobRecordEvent(pipelineRecord, jobRecord, triggerUser);
                break;
            }
            default: {
                throw new NotSupportOperateException();
            }
        }
    }

    private void restartJobEvent(PipelineRecord pipelineRecord, JobRecord jobRecord, String triggerUser, JSONObject jobParams) {
        JobRecord newJobRecord;
        if (jobRecord.getStatus() == BuildStatus.PENDING) {
            newJobRecord = jobRecord;
        } else {
            BuildStatus status = jobRecord.getStatus();
            if (BuildStatus.WAITING == status || BuildStatus.FAIL == status || BuildStatus.CANCEL == status || BuildStatus.ABORT == status) {
                log.info("start re init job:[{}] ,type:[{}], plugin:[{}]", jobRecord.getId(), jobRecord.getPluginType(), jobRecord.getJobType());
                Pipeline pipeline = pipelineService.getByIdIfPresent(pipelineRecord.getPipelineId());
                RepoVo repo = repoService.getByRepoKeyIfPresent(pipeline.getCompanyId(), pipeline.getRepoKey());
                newJobRecord = jobRecordService.reInitJobRecord(repo, pipelineRecord, jobRecord);
            } else {
                throw new OperateNotPermittedException();
            }
        }

        startJobRecordEvent(pipelineRecord, newJobRecord, triggerUser, jobParams, true);
    }

    private void triggerNextWaitingJob(PipelineRecord pipelineRecord, JobRecord jobRecord) {
        if (BuildStatus.SUCCESS == jobRecord.getStatus() || BuildStatus.SKIP == jobRecord.getStatus()) {
            JobRecord runningJob = jobRecordService.getRunningJob(jobRecord);
            if (runningJob != null) {
                return;
            }

            JobRecord nextWaitingJob = jobRecordService.getNextWaitingJob(jobRecord);
            if (nextWaitingJob == null) {
                return;
            }

            startJobRecordEvent(pipelineRecord, nextWaitingJob, pipelineRecord.getTriggerUser(), null, false);
        }
    }

    private void startJob(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord, String triggerUser, JSONObject jobParams) {
        Plugin plugin = pluginService.getPlugin(jobRecord.getJobType());
        PluginOperator pluginOperator = plugin.getPluginOperator();
        jobRecord.setTriggerUser(triggerUser);

        // 不在这里更新状态，由插件通知job开始
        jobRecordService.updateJobRecord(jobRecord);

        try {
            // 更新参数
            if (jobParams != null) {
                pluginService.updatePluginBuild(jobRecord, jobParams);
            }

            log.info("exec job id:[{}], type:[{}], plugin:[{}]", jobRecord.getId(), jobRecord.getPluginType(), jobRecord.getJobType());
            updateJobRecordEvent(pipelineRecord, jobRecord, BuildStatus.RUNNING, false, StringUtils.EMPTY);

            boolean execute = pluginOperator.execute(repo, pipeline, pipelineRecord, jobRecord);
            if (!execute) {
                return;
            }

            // 更新下游插件变更的属性
            jobRecordService.updateJobRecord(jobRecord);

            crontabClient.registerJobTimeout(jobRecord, pipelineRecord.getJobTimeoutMinute());
            noticeService.asyncNoticeJobStart(repo, pipelineRecord, jobRecord);
            pluginOperator.jobStartCallback(pipelineRecord, jobRecord, StringUtils.EMPTY);
        } catch (Exception exception) {
            String message = StringUtils.substring(exception.getMessage(), 0, MESSAGE_MAX_LENGTH);
            updateJobRecordEvent(pipelineRecord, jobRecord, BuildStatus.FAIL, false, message);
        }
    }


    private void cancelJob(PipelineRecord pipelineRecord, JobRecord jobRecord, String triggerUser) {
        Pipeline pipeline = pipelineService.getByIdIfPresent(pipelineRecord.getPipelineId());
        RepoVo repo = repoService.getByRepoKeyIfPresent(pipeline.getCompanyId(), pipeline.getRepoKey());
        log.info("cancel job:[{}], status:[{}]", jobRecord.getId(), jobRecord.getStatus());
        pluginService.cancelPluginBuild(pipelineRecord, jobRecord, triggerUser);
        noticeService.asyncNoticeJobFailed(repo, pipelineRecord, jobRecord);
    }


}
