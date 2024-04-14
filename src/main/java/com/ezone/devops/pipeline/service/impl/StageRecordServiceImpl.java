package com.ezone.devops.pipeline.service.impl;

import com.ezone.devops.pipeline.common.BuildStatus;
import com.ezone.devops.pipeline.dao.StageRecordDao;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.model.Stage;
import com.ezone.devops.pipeline.model.StageRecord;
import com.ezone.devops.pipeline.service.JobRecordService;
import com.ezone.devops.pipeline.service.StageRecordService;
import com.ezone.devops.pipeline.service.StageService;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.galaxy.framework.common.bean.BaseException;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StageRecordServiceImpl implements StageRecordService {

    @Autowired
    private StageService stageService;
    @Autowired
    private StageRecordDao stageRecordDao;
    @Autowired
    private JobRecordService jobRecordService;

    @Override
    public StageRecord getByIdIfPresent(Long id) {
        StageRecord stageRecord = stageRecordDao.get(id);
        if (stageRecord == null) {
            throw new BaseException(HttpStatus.NOT_FOUND.value(), "阶段构建记录不存在");
        }
        return stageRecord;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void initStageRecords(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineBuild) {
        List<Stage> stages = stageService.getOrderedStages(pipeline);
        Long upstreamId = StageRecord.HEAD_STAGE_ID;
        for (Stage stage : stages) {
            StageRecord stageRecord = new StageRecord(pipelineBuild, stage, upstreamId);
            stageRecord = stageRecordDao.save(stageRecord);
            jobRecordService.initJobRecords(repo, pipeline, pipelineBuild, stageRecord, stage);
            upstreamId = stageRecord.getId();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StageRecord updateStageRecord(StageRecord stageBuild) {
        stageRecordDao.update(stageBuild);
        return stageBuild;
    }

    @Override
    public StageRecord getFirstStage(PipelineRecord pipelineRecord) {
        return stageRecordDao.getFirstStageByPipelineBuildId(pipelineRecord.getId());
    }

    @Override
    public List<StageRecord> getOrderedStageRecordByPipelineRecord(PipelineRecord pipelineRecord) {
        List<StageRecord> stageBuilds = stageRecordDao.getByPipelineBuildId(pipelineRecord.getId());
        orderStageRecords(stageBuilds);
        return stageBuilds;
    }

    @Override
    public List<StageRecord> getStageRecordByPipelineRecords(List<PipelineRecord> pipelineRecords) {
        if (CollectionUtils.isEmpty(pipelineRecords)) {
            return null;
        }
        Set<Long> pipelineBuildIds = pipelineRecords.stream().map(PipelineRecord::getId).collect(Collectors.toSet());
        return stageRecordDao.getByPipelineBuildIds(pipelineBuildIds);

    }

    @Override
    public List<StageRecord> getParentStageRecords(StageRecord stageBuild) {
        return stageRecordDao.getParentStageRecords(stageBuild.getPipelineRecordId(), stageBuild.getId());
    }

    @Override
    public StageRecord getNextStageRecord(StageRecord stageBuild) {
        List<StageRecord> stageRecords = stageRecordDao.getByPipelineBuildId(stageBuild.getPipelineRecordId());
        for (StageRecord stageRecord : stageRecords) {
            if (stageRecord.getStatus() == BuildStatus.WAITING || stageRecord.getStatus() == BuildStatus.PENDING) {
                return stageRecord;
            }
        }

        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByPipeline(Pipeline pipeline) {
        stageRecordDao.deleteByPipelineId(pipeline.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteByPipelineRecord(PipelineRecord pipelineRecord) {
        jobRecordService.deleteByPipelineRecord(pipelineRecord);
        stageRecordDao.deleteByPipelineRecordId(pipelineRecord.getId());
        return true;
    }

    private void orderStageRecords(List<StageRecord> stageBuilds) {
        if (CollectionUtils.isEmpty(stageBuilds)) {
            return;
        }
        Map<Long, StageRecord> stageBuildMap = Maps.newHashMap();
        for (StageRecord stageRecord : stageBuilds) {
            stageBuildMap.put(stageRecord.getUpstreamId(), stageRecord);
        }
        stageBuilds.clear();
        StageRecord firstBuild = stageBuildMap.get(0L);
        while (firstBuild != null) {
            stageBuilds.add(firstBuild);
            firstBuild = stageBuildMap.get(firstBuild.getId());
        }
    }
}
