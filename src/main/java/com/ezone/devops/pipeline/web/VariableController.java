package com.ezone.devops.pipeline.web;

import com.ezone.devops.pipeline.annotations.PipelinePermission;
import com.ezone.devops.pipeline.context.PipelineContext;
import com.ezone.devops.pipeline.enums.PipelinePermissionType;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.service.VariableService;
import com.ezone.ezbase.iam.bean.constant.CookieConstant;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.bean.HttpCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "流水线环境变量接口api")
@Slf4j
@RestController
@RequestMapping("/pipelines")
public class VariableController {

    @Autowired
    private VariableService variableService;


    @PipelinePermission(pipelineId = "#id", requiredPermission = PipelinePermissionType.PIPELINE_READER)
    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @ApiOperation("查询流水线的环境变量")
    @GetMapping("/{id}/variables")
    public BaseResponse<?> getPipeline(@PathVariable Long id) {
        Pipeline pipeline = PipelineContext.get();
        return new BaseResponse<>(HttpCode.SUCCESS, variableService.filterSecretVariable(pipeline));
    }
}

