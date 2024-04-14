package com.ezone.devops.pipeline.web.internal;

import com.ezone.devops.pipeline.common.ScmTriggerType;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.model.StageRecord;
import com.ezone.devops.pipeline.service.PipelineRecordService;
import com.ezone.devops.pipeline.service.StageRecordService;
import com.ezone.devops.pipeline.web.response.PipelineRecordResponse;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.bean.HttpCode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

@Slf4j
@ApiIgnore
@RestController
@RequestMapping("/internal/builds")
public class PipelineBuildInternalController {

    @Autowired
    private PipelineRecordService pipelineRecordService;
    @Autowired
    private StageRecordService stageRecordService;

    /**
     * 代码库在使用
     */
    @GetMapping
    public BaseResponse<?> pipelineBuildList(@RequestParam Long companyId,
                                             @RequestParam("repoId") String repoKey,
                                             @RequestParam(required = false) Long pipelineId,
                                             @RequestParam(required = false, defaultValue = "BRANCH") ScmTriggerType scmTriggerType,
                                             @RequestParam(required = false) String branchName,
                                             @RequestParam String externalKey) {
        List<PipelineRecord> pipelineRecords = pipelineRecordService.getPipelineRecords(companyId, repoKey, pipelineId, scmTriggerType, branchName, externalKey);
        return new BaseResponse<>(HttpCode.SUCCESS, convert(pipelineRecords));
    }

    private List<PipelineRecordResponse> convert(List<PipelineRecord> pipelineRecords) {
        List<StageRecord> stageRecords = stageRecordService.getStageRecordByPipelineRecords(pipelineRecords);
        Map<Long, List<StageRecord>> groupStageBuilds = groupStageBuild(stageRecords);
        List<PipelineRecordResponse> result = Lists.newArrayList();
        for (PipelineRecord pipelineRecord : pipelineRecords) {
            result.add(new PipelineRecordResponse(pipelineRecord, groupStageBuilds.get(pipelineRecord.getId())));
        }
        return result;
    }

    private Map<Long, List<StageRecord>> groupStageBuild(List<StageRecord> stageRecords) {
        Map<Long, List<StageRecord>> relations = Maps.newHashMap();
        if (CollectionUtils.isEmpty(stageRecords)) {
            return relations;
        }
        for (StageRecord stageRecord : stageRecords) {
            Long pipelineBuildId = stageRecord.getPipelineRecordId();
            if (relations.containsKey(pipelineBuildId)) {
                relations.get(pipelineBuildId).add(stageRecord);
            } else {
                relations.put(pipelineBuildId, Lists.newArrayList(stageRecord));
            }
        }
        return relations;
    }
}

