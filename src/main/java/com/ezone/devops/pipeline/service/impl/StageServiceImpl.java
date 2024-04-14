package com.ezone.devops.pipeline.service.impl;

import com.ezone.devops.pipeline.dao.StageDao;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.Stage;
import com.ezone.devops.pipeline.service.JobService;
import com.ezone.devops.pipeline.service.StageService;
import com.ezone.devops.pipeline.web.request.GroupJob;
import com.ezone.devops.pipeline.web.request.StagePayload;
import com.ezone.galaxy.framework.common.bean.BaseException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class StageServiceImpl implements StageService {

    @Autowired
    private StageDao stageDao;
    @Autowired
    private JobService jobService;
    // 第一个stage没有上游，用0表示
    private static final Long START_STAGE_ID = 0L;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveStage(Pipeline pipeline, List<StagePayload> payloads) {
        log.info("start save stage, pipeline:[{}]", pipeline);
        if (CollectionUtils.isEmpty(payloads)) {
            return;
        }

        saveAllStage(pipeline, payloads);
        log.info("finished save stage, pipeline:[{}]", pipeline);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStage(Pipeline pipeline, List<StagePayload> payloads) {
        log.info("start save stage, pipeline:[{}]", pipeline);
        if (CollectionUtils.isEmpty(payloads)) {
            return;
        }
        deleteStage(pipeline);
        saveAllStage(pipeline, payloads);
        log.info("finished save stage, pipeline:[{}]", pipeline);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteStage(Pipeline pipeline) {
        List<Stage> stages = stageDao.getByPipelineId(pipeline.getId());
        if (CollectionUtils.isEmpty(stages)) {
            return false;
        }
        jobService.deleteJobs(pipeline, stages);
        return stageDao.delete(stages) > 0;
    }

    @Override
    public Map<Long, List<Stage>> getStageGroup(Collection<Long> pipelineIds) {
        if (CollectionUtils.isEmpty(pipelineIds)) {
            return null;
        }
        List<Stage> stages = stageDao.getByPipelineIds(pipelineIds);
        return groupStage(stages);
    }

    private Map<Long, List<Stage>> groupStage(List<Stage> stages) {
        if (CollectionUtils.isEmpty(stages)) {
            return null;
        }
        Map<Long, List<Stage>> groups = Maps.newHashMap();
        for (Stage stage : stages) {
            Long pipelineId = stage.getPipelineId();
            if (groups.containsKey(pipelineId)) {
                groups.get(pipelineId).add(stage);
            } else {
                groups.put(pipelineId, Lists.newArrayList(stage));
            }
        }
        return groups;
    }

    /**
     * 更新上游id并保存
     */
    private void saveAllStage(Pipeline pipeline, List<StagePayload> stagePayloads) {
        Long upstreamId = START_STAGE_ID;
        for (StagePayload stagePayload : stagePayloads) {
            Stage stage = new Stage(pipeline, stagePayload, upstreamId);
            stageDao.save(stage);

            List<GroupJob> groupJobs = stagePayload.getGroupJobs();
            if (CollectionUtils.isEmpty(groupJobs)) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "阶段下的任务不允许为空");
            }

            // 处理job的配置
            log.debug("start save stage job group:[{}]", groupJobs);
            boolean saveJobResult = jobService.saveJobs(pipeline, stage, groupJobs);
            log.debug("finished save stage job group:[{}] saveJobResult:[{}]", groupJobs, saveJobResult);

            upstreamId = stage.getId();
        }

    }

    @Override
    public List<Stage> getStages(Long pipelineId) {
        return stageDao.getByPipelineId(pipelineId);
    }

    @Override
    public List<Stage> getOrderedStages(Pipeline pipeline) {
        List<Stage> stages = getStages(pipeline.getId());
        orderStages(stages);
        return stages;
    }

    @Override
    public List<StagePayload> getStagePayloads(Pipeline pipeline) {
        List<Stage> orderedStages = getOrderedStages(pipeline);
        if (CollectionUtils.isEmpty(orderedStages)) {
            return null;
        }
        List<StagePayload> stagePayloads = Lists.newArrayListWithCapacity(orderedStages.size());
        for (Stage stage : orderedStages) {
            StagePayload stagePayload = new StagePayload();
            stagePayload.setStageName(stage.getName());

            List<GroupJob> groupJobs = jobService.getOrderedJobPayloads(pipeline, stage);
            stagePayload.setGroupJobs(groupJobs);
            stagePayloads.add(stagePayload);
        }
        return stagePayloads;
    }

    private void orderStages(List<Stage> stages) {
        Map<Long, Stage> stageConfMap = Maps.newHashMap();
        for (Stage stage : stages) {
            stageConfMap.put(stage.getUpstreamId(), stage);
        }

        stages.clear();
        Stage firstStageConfig = stageConfMap.get(0L);
        while (firstStageConfig != null) {
            stages.add(firstStageConfig);
            firstStageConfig = stageConfMap.get(firstStageConfig.getId());
        }
    }

}
