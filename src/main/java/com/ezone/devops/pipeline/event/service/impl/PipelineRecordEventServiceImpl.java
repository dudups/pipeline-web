package com.ezone.devops.pipeline.event.service.impl;

import com.ezone.devops.pipeline.common.BuildStatus;
import com.ezone.devops.pipeline.common.ExecutorTaskPool;
import com.ezone.devops.pipeline.event.service.PipelineRecordEventService;
import com.ezone.devops.pipeline.event.service.StageRecordEventService;
import com.ezone.devops.pipeline.exception.OperateNotPermittedException;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.model.StageRecord;
import com.ezone.devops.pipeline.mq.bean.WebsocketEvent;
import com.ezone.devops.pipeline.mq.producer.WebsocketProducer;
import com.ezone.devops.pipeline.service.PipelineHookService;
import com.ezone.devops.pipeline.service.PipelineRecordService;
import com.ezone.devops.pipeline.service.StageRecordService;
import com.ezone.devops.pipeline.util.BuildStatusUtil;
import com.ezone.devops.pipeline.web.response.PipelineRecordResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 流水线的执行逻辑实现
 */
@Slf4j
@Service
public class PipelineRecordEventServiceImpl implements PipelineRecordEventService {

    @Autowired
    private PipelineRecordService pipelineRecordService;
    @Autowired
    private StageRecordService stageRecordService;
    @Autowired
    private StageRecordEventService stageRecordEventService;
    @Autowired
    private PipelineHookService pipelineHookService;
    @Autowired
    private ExecutorTaskPool executorTaskPool;
    @Autowired
    private WebsocketProducer websocketProducer;
    private ExecutorService pipelineHookExecutor = Executors.newFixedThreadPool(10);

    @Override
    public boolean startPipelineRecordEvent(PipelineRecord pipelineRecord, String triggerUser) {
        executorTaskPool.execute(() -> startPipeline(pipelineRecord, triggerUser));
        return true;
    }

    @Override
    public void cancelPipelineRecordEvent(PipelineRecord pipelineRecord, String triggerUser) {
        if (BuildStatus.isEnd(pipelineRecord.getStatus())) {
            throw new OperateNotPermittedException();
        }

        List<StageRecord> stageRecords = stageRecordService.getOrderedStageRecordByPipelineRecord(pipelineRecord);
        for (StageRecord stageRecord : stageRecords) {
            if (BuildStatus.isRunning(stageRecord.getStatus())) {
                stageRecordEventService.cancelBuildRecordEvent(pipelineRecord, stageRecord, triggerUser);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updatePipelineRecordEvent(PipelineRecord pipelineRecord) {
        log.info("receive update pipeline status event, pipelineRecord:[{}]", pipelineRecord.getId());
        List<StageRecord> orderedStages = stageRecordService.getOrderedStageRecordByPipelineRecord(pipelineRecord);
        Set<BuildStatus> buildStatuses = orderedStages.stream().map(StageRecord::getStatus).collect(Collectors.toSet());
        BuildStatus buildStatus = BuildStatusUtil.getPipelineStatus(buildStatuses);
        if (pipelineRecord.getStatus() == buildStatus) {
            log.info("pipeline:[{}] status not changed, status:[{}]", pipelineRecord.getId(), buildStatus);
            return;
        }

        if (pipelineRecord.getCreateTime() == null) {
            pipelineRecord.setCreateTime(new Date());
        }

        log.info("update pipeline build, pipelineRecord:[{}] status from:[{}] to:[{}]",
                pipelineRecord.getId(), pipelineRecord.getStatus(), buildStatus);

        pipelineRecord.setModifyTime(new Date());
        pipelineRecord.setStatus(buildStatus);
        pipelineRecordService.updatePipelineRecord(pipelineRecord);

        pipelineHookExecutor.execute(() -> {
            websocketProducer.sendEvent(WebsocketEvent.RECORDS, pipelineRecord.getId(), new PipelineRecordResponse(pipelineRecord, orderedStages));
            pipelineHookService.triggerHook(pipelineRecord);
        });
    }

    private void startPipeline(PipelineRecord pipelineRecord, String triggerUser) {
        pipelineHookExecutor.execute(() -> pipelineHookService.noticeStart(pipelineRecord));
        log.info("start execute pipeline:[{}]", pipelineRecord.getId());
        StageRecord firstStage = stageRecordService.getFirstStage(pipelineRecord);
        stageRecordEventService.startBuildRecordEvent(pipelineRecord, firstStage, triggerUser);
    }
}
