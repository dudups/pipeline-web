package com.ezone.devops.pipeline.web.internal;

import com.ezone.devops.pipeline.common.TriggerMode;
import com.ezone.devops.pipeline.enums.PipelinePermissionType;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.mq.bean.TriggerMessage;
import com.ezone.devops.pipeline.service.*;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.pipeline.web.request.TriggerConfigPayload;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.bean.HttpCode;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Api(tags = "内部使用接口")
@RestController
@RequestMapping("/internal/ezcode")
public class EzCodeInternalController {

    @Autowired
    private TriggerPipelineService triggerPipelineService;
    @Autowired
    private PipelineTriggerService pipelineTriggerService;
    @Autowired
    private BranchPatternConfigService branchPatternConfigService;
    @Autowired
    private PipelinePermissionService pipelinePermissionService;
    @Autowired
    private PipelineService pipelineService;

    @GetMapping("/pipeline_map")
    public BaseResponse<?> listPipelines(@RequestParam Set<String> repoKeys) {
        return new BaseResponse<>(HttpCode.SUCCESS, pipelineService.getByRepoKeys(repoKeys));
    }

    @PostMapping("/pipelines")
    public BaseResponse<?> triggerPipeline(@RequestBody TriggerMessage message) {
        log.info("receive trigger message: {}.", message);
        TriggerMode triggerMode = message.getTriggerMode();
        if (Objects.isNull(triggerMode)) {
            log.error("Invalid trigger type: {}.", message.getTriggerType());
            return new BaseResponse<>(HttpCode.SUCCESS);
        }

        RepoVo repo = new RepoVo();
        repo.setCompanyId(message.getCompanyId());
        repo.setLongRepoKey(message.getRepoId());
        repo.setRepoKey(String.valueOf(message.getRepoId()));
        repo.setRepoName(message.getRepoName());

        String branchName = message.getBranchName();
        Set<Pipeline> matchedPipelines = branchPatternConfigService.getMatchedPipelines(repo, branchName);
        if (CollectionUtils.isEmpty(matchedPipelines)) {
            return new BaseResponse<>(HttpCode.SUCCESS);
        }

        List<PipelineRecord> pipelineRecords = Lists.newArrayList();
        matchedPipelines.forEach(pipeline -> {
            String operator = message.getOperator();
            boolean hasPermission = pipelinePermissionService.hasPermission(repo, pipeline, PipelinePermissionType.PIPELINE_OPERATOR, operator);
            // 过滤掉没有权限的流水线
            if (hasPermission) {
                TriggerConfigPayload payload = pipelineTriggerService.getTriggerConfigPayload(pipeline);
                if (needToTriggerPipeline(payload, triggerMode)) {
                    PipelineRecord pipelineRecord = triggerPipelineService.triggerPipelineOnlyForBranch(pipeline, repo, message);
                    pipelineRecords.add(pipelineRecord);
                }
            }
        });

        return new BaseResponse<>(HttpCode.SUCCESS, pipelineRecords);
    }

    /**
     * 非上游（代码库）触发，或未配置该事件，无需触发流水线
     */
    private boolean needToTriggerPipeline(TriggerConfigPayload payload, TriggerMode triggerMode) {
        if (Objects.isNull(payload) || payload.getTriggerMode() != TriggerMode.UPSTREAM) {
            return false;
        }
        return CollectionUtils.isNotEmpty(payload.getCiEvents()) && payload.getCiEvents().contains(triggerMode);
    }
}

