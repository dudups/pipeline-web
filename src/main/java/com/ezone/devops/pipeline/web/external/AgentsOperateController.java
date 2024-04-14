package com.ezone.devops.pipeline.web.external;

import com.ezone.devops.pipeline.annotations.UserTokenPermission;
import com.ezone.devops.pipeline.context.PipelineContext;
import com.ezone.devops.pipeline.context.PipelineRecordContext;
import com.ezone.devops.pipeline.enums.PipelinePermissionType;
import com.ezone.devops.pipeline.service.RuntimeVariableService;
import com.ezone.devops.pipeline.web.request.RuntimeVariablePayload;
import com.ezone.ezbase.iam.bean.constant.CookieConstant;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.bean.HttpCode;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@ApiIgnore
@RestController
@RequestMapping("/agents/pipelines/{pipelineId}/builds")
public class AgentsOperateController {

    @Autowired
    private RuntimeVariableService runtimeVariableService;

    @UserTokenPermission(requiredPermission = PipelinePermissionType.PIPELINE_OPERATOR)
    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "设置流水线的运行时环境变量")
    @PostMapping("/{recordId}/variables")
    public BaseResponse<?> mergeVariables(@PathVariable Long pipelineId,
                                          @PathVariable Long recordId,
                                          @RequestBody @Valid RuntimeVariablePayload payload) {
        return new BaseResponse<>(HttpCode.SUCCESS, runtimeVariableService.mergeRuntimeVariables(PipelineContext.get(), PipelineRecordContext.get(), payload.getVariables()));
    }

}

