package com.ezone.devops.pipeline.web.internal;

import com.ezone.devops.pipeline.common.ScmTriggerType;
import com.ezone.devops.pipeline.enums.PipelinePermissionType;
import com.ezone.devops.pipeline.exception.NoPermissionOperateException;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.service.PipelinePermissionService;
import com.ezone.devops.pipeline.service.PipelineService;
import com.ezone.devops.pipeline.service.RepoService;
import com.ezone.devops.pipeline.service.TriggerPipelineService;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.pipeline.web.request.InternalTriggerPipelinePayload;
import com.ezone.devops.pipeline.web.request.TriggerPipelinePayload;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.bean.HttpCode;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Api(tags = "内部使用接口")
@RestController
@RequestMapping("/internal")
public class PipelineTriggerInternalController {

    @Autowired
    private TriggerPipelineService triggerPipelineService;
    @Autowired
    private PipelineService pipelineService;
    @Autowired
    private RepoService repoService;
    @Autowired
    private PipelinePermissionService pipelinePermissionService;

    /**
     * 定时任务的功能在使用
     *
     * @param pipelineId
     * @return
     */
    @PostMapping("/triggers/crontab/{pipelineId}")
    public BaseResponse<?> crontabBuild(@PathVariable Long pipelineId) {
        Pipeline pipeline = pipelineService.getByIdIfPresent(pipelineId);
        RepoVo repo = repoService.getByRepoKey(pipeline.getCompanyId(), pipeline.getRepoKey());

        boolean hasPermission = pipelinePermissionService.hasPermission(repo, pipeline, PipelinePermissionType.PIPELINE_OPERATOR, pipeline.getCreateUser());
        if (hasPermission) {
            triggerPipelineService.asyncCrontabTriggerPipeline(pipeline);
        }

        return new BaseResponse<>(HttpCode.SUCCESS);
    }

    /**
     * 流水线自己的触发流水线插件在使用
     *
     * @param pipelineId
     * @param payload
     * @return
     */
    @PostMapping("/pipelines/{pipelineId}")
    public BaseResponse<?> triggerPipeline(@PathVariable Long pipelineId,
                                           @RequestBody @Valid InternalTriggerPipelinePayload payload) {
        Pipeline pipeline = pipelineService.getByIdIfPresent(pipelineId);
        RepoVo repo = repoService.getByRepoKeyIfPresent(pipeline.getCompanyId(), pipeline.getRepoKey());

        boolean hasPermission = pipelinePermissionService.hasPermission(repo, pipeline, PipelinePermissionType.PIPELINE_OPERATOR, pipeline.getCreateUser());
        if (!hasPermission) {
            throw new NoPermissionOperateException();
        }

        TriggerPipelinePayload triggerPipelinePayload = new TriggerPipelinePayload();
        triggerPipelinePayload.setExternalName(payload.getExternalName());
        triggerPipelinePayload.setScmTriggerType(ScmTriggerType.BRANCH);

        PipelineRecord pipelineRecord = triggerPipelineService.triggerPipeline(pipeline, triggerPipelinePayload,
                payload.getTriggerUser(), payload.getTriggerMode(), payload.getCallbackUrl());
        return new BaseResponse<>(HttpCode.SUCCESS, pipelineRecord);
    }

}

