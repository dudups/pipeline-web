package com.ezone.devops.pipeline.event.service.impl;

import com.ezone.devops.pipeline.common.BuildStatus;
import com.ezone.devops.pipeline.common.ExecutorTaskPool;
import com.ezone.devops.pipeline.config.SystemConfig;
import com.ezone.devops.pipeline.enums.OperationType;
import com.ezone.devops.pipeline.event.service.JobRecordEventService;
import com.ezone.devops.pipeline.event.service.PipelineRecordEventService;
import com.ezone.devops.pipeline.event.service.StageRecordEventService;
import com.ezone.devops.pipeline.exception.NotSupportOperateException;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.model.StageRecord;
import com.ezone.devops.pipeline.service.JobRecordService;
import com.ezone.devops.pipeline.service.PipelineRecordService;
import com.ezone.devops.pipeline.service.StageRecordService;
import com.ezone.devops.pipeline.util.BuildStatusUtil;
import com.ezone.galaxy.framework.common.bean.BaseException;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
 * 事件驱动执行stageBuild的服务类
 */
@Slf4j
@Service
public class StageRecordEventServiceImpl implements StageRecordEventService {

    private static final String OPERATOR_BY_WHO = "此任务已由%s跳过，任务跳过后将无法再执行";
    private static final String OPERATION_KEY_PREFIX = "stage:operation:key:";
    @Autowired
    private SystemConfig systemConfig;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private PipelineRecordService pipelineRecordService;
    @Autowired
    private PipelineRecordEventService pipelineRecordEventService;
    @Autowired
    private StageRecordService stageRecordService;
    @Autowired
    private JobRecordService jobRecordService;
    @Autowired
    private JobRecordEventService jobRecordEventService;
    @Autowired
    private ExecutorTaskPool executorTaskPool;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void startBuildRecordEvent(PipelineRecord pipelineRecord, StageRecord stageRecord, String triggerUser) {
        stageRecord.setStatus(BuildStatus.WAITING);
        stageRecordService.updateStageRecord(stageRecord);
        log.info("start trigger pipeline:[{}] first stage:[{}]", pipelineRecord.getId(), stageRecord.getId());
        startStage(pipelineRecord, stageRecord, triggerUser);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancelBuildRecordEvent(PipelineRecord pipelineRecord, StageRecord stageRecord, String triggerUser) {
        stageRecord.setStatus(BuildStatus.CANCEL);
        stageRecordService.updateStageRecord(stageRecord);

        // 通知流水线更新状态
        pipelineRecordEventService.updatePipelineRecordEvent(pipelineRecord);

        executorTaskPool.execute(() -> cancelStage(pipelineRecord, stageRecord, triggerUser));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void skipBuildRecordEvent(PipelineRecord pipelineRecord, StageRecord stageRecord, String triggerUser) {
        if (stageRecord.getStatus() == BuildStatus.WAITING || stageRecord.getStatus() == BuildStatus.PENDING) {
            List<JobRecord> jobRecords = jobRecordService.getJobRecords(stageRecord);
            Date date = new Date();
            for (JobRecord jobRecord : jobRecords) {
                if (BuildStatus.isEnd(jobRecord.getStatus())) {
                    continue;
                }

                jobRecord.setStatus(BuildStatus.SKIP);
                jobRecord.setModifyTime(date);
                jobRecord.setMessage(String.format(OPERATOR_BY_WHO, triggerUser));
                jobRecordService.updateJobRecord(jobRecord);
            }

            stageRecord.setStatus(BuildStatus.SKIP);
            stageRecord.setModifyTime(date);
            stageRecordService.updateStageRecord(stageRecord);

            //  通知流水线更新
            pipelineRecordEventService.updatePipelineRecordEvent(pipelineRecord);

            // 触发下一个stage
            triggerNextStageBuild(pipelineRecord, stageRecord);
        } else {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "已经存在运行的任务，无法执行跳过");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void stageOperator(PipelineRecord pipelineRecord, StageRecord stageRecord, String triggerUser, OperationType operationType) {
        Long stageRecordId = stageRecord.getId();
        // 检查操作频率
        String key = OPERATION_KEY_PREFIX.concat(String.valueOf(stageRecordId));
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, String.valueOf(stageRecordId), systemConfig.getReExecuteLimit(), TimeUnit.SECONDS);
        if (result != null && !result) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "同一条流水线构建操作太频繁，请间隔2秒后再执行");
        }

        switch (operationType) {
//            case START: {
//                startStage(pipelineRecord, stageRecord, triggerUser);
//                break;
//            }
//            case CANCEL: {
//                cancelStage(pipelineRecord, stageRecord, triggerUser);
//                break;
//            }
            case SKIP_SINGLE: {
                skipBuildRecordEvent(pipelineRecord, stageRecord, triggerUser);
                break;
            }
            default: {
                throw new NotSupportOperateException();
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStageRecordEvent(Long stageBuildId) {
        StageRecord stageRecord = stageRecordService.getByIdIfPresent(stageBuildId);
        List<JobRecord> jobRecords = jobRecordService.getJobRecords(stageRecord);
        Set<BuildStatus> jobStatuses = jobRecords.stream().map(JobRecord::getStatus).collect(Collectors.toSet());
        BuildStatus buildStatus = BuildStatusUtil.getStageStatus(jobStatuses);
        // 状态没改变，不做更新
        if (stageRecord.getStatus() == buildStatus) {
            log.info("pipeline:[{}] of stage:[{}] status not changed, status:[{}]", stageRecord.getPipelineRecordId(), stageRecord.getId(), buildStatus);
            return;
        }

        log.info("update stage status, pipeline:[{}] stage:[{}] status from:[{}] to:[{}]", stageRecord.getPipelineRecordId(), stageRecord.getId(), stageRecord.getStatus(), buildStatus);
        stageRecord.setStatus(buildStatus);
        stageRecord.setModifyTime(new Date());
        stageRecordService.updateStageRecord(stageRecord);

        //  通知流水线更新
        PipelineRecord pipelineRecord = pipelineRecordService.getByIdIfPresent(stageRecord.getPipelineRecordId());
        log.info("notice pipeline:[{}] change status", stageRecord.getPipelineRecordId());
        pipelineRecordEventService.updatePipelineRecordEvent(pipelineRecord);

        // 触发下一个stage
        triggerNextStageBuild(pipelineRecord, stageRecord);
    }

    private void triggerNextStageBuild(PipelineRecord pipelineRecord, StageRecord stageRecord) {
        if (BuildStatus.SUCCESS == stageRecord.getStatus() || BuildStatus.SKIP == stageRecord.getStatus()) {
            List<StageRecord> parentStageRecords = stageRecordService.getParentStageRecords(stageRecord);
            if (CollectionUtils.isNotEmpty(parentStageRecords)) {
                Set<BuildStatus> parentStageStatuses = parentStageRecords.stream().map(StageRecord::getStatus).collect(Collectors.toSet());
                if (CollectionUtils.containsAny(parentStageStatuses, Sets.newHashSet(BuildStatus.WAITING, BuildStatus.PENDING, BuildStatus.RUNNING))) {
                    return;
                }
            }

            StageRecord nextStageRecord = stageRecordService.getNextStageRecord(stageRecord);
            if (null == nextStageRecord) {
                return;
            }


            startBuildRecordEvent(pipelineRecord, nextStageRecord, pipelineRecord.getTriggerUser());
        }
    }

    private void startStage(PipelineRecord pipelineRecord, StageRecord stageRecord, String triggerUser) {
        List<JobRecord> headJobRecords = jobRecordService.getMiniPipelineHeadJobRecords(stageRecord);
        if (CollectionUtils.isEmpty(headJobRecords)) {
            return;
        }

        for (JobRecord jobRecord : headJobRecords) {
            if (jobRecord.getStatus() == BuildStatus.SKIP) {
                jobRecord = jobRecordService.getNextWaitingJob(jobRecord);
            }
            if (jobRecord == null) {
                continue;
            }

            jobRecordEventService.startJobRecordEvent(pipelineRecord, jobRecord, triggerUser, null, false);
        }
    }

    private void cancelStage(PipelineRecord pipelineRecord, StageRecord stageRecord, String triggerUser) {
        List<JobRecord> jobRecords = jobRecordService.getJobRecords(stageRecord);
        for (JobRecord jobRecord : jobRecords) {
            if (BuildStatus.isEnd(jobRecord.getStatus())) {
                continue;
            }
            jobRecordEventService.cancelJobRecordEvent(pipelineRecord, jobRecord, triggerUser);
        }
    }
}
