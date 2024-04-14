package com.ezone.devops.pipeline.web;

import com.ezone.devops.pipeline.annotations.StagePermission;
import com.ezone.devops.pipeline.context.StageRecordContext;
import com.ezone.devops.pipeline.enums.PipelinePermissionType;
import com.ezone.devops.pipeline.event.service.StageRecordEventService;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.model.StageRecord;
import com.ezone.devops.pipeline.service.PipelineRecordService;
import com.ezone.devops.pipeline.web.request.OperationPayload;
import com.ezone.ezbase.iam.bean.constant.CookieConstant;
import com.ezone.ezbase.iam.service.AuthUtil;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.bean.HttpCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "阶段级别构建api")
@Validated
@RestController
@RequestMapping("/pipelines/{pipelineId}/stages")
public class StageRecordController {

    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private StageRecordEventService stageRecordEventService;
    @Autowired
    private PipelineRecordService pipelineRecordService;


    @StagePermission(requiredPermission = PipelinePermissionType.PIPELINE_OPERATOR)
    @ApiOperation("阶段的跳过执行")
    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @PutMapping("/{stageId}")
    public BaseResponse<?> stageOperator(@PathVariable Long pipelineId,
                                         @PathVariable Long stageId,
                                         @RequestBody OperationPayload payload) {
        StageRecord stageRecord = StageRecordContext.get();
        PipelineRecord pipelineRecord = pipelineRecordService.getByIdIfPresent(pipelineId, stageRecord.getPipelineRecordId());
        stageRecordEventService.stageOperator(pipelineRecord, stageRecord, authUtil.getUsername(), payload.getOperationType());
        return new BaseResponse<>(HttpCode.SUCCESS);
    }
}

