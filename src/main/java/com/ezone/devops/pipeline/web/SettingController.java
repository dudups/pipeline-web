package com.ezone.devops.pipeline.web;

import com.ezone.devops.pipeline.annotations.CompanyAdminPermission;
import com.ezone.devops.pipeline.model.PipelineSetting;
import com.ezone.devops.pipeline.service.PipelineSettingService;
import com.ezone.devops.pipeline.web.request.PipelineSettingPayload;
import com.ezone.ezbase.iam.bean.constant.CookieConstant;
import com.ezone.ezbase.iam.service.AuthUtil;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.bean.HttpCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Api(tags = "流水线设置api")
@Validated
@RestController
@RequestMapping("/settings")
public class SettingController {

    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private PipelineSettingService pipelineSettingService;

    @CompanyAdminPermission
    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @ApiOperation("自动清理时间查询接口")
    @GetMapping
    public BaseResponse<?> getPipelineSetting() {
        PipelineSetting pipelineSetting = pipelineSettingService.getSetting(authUtil.getCompanyId());
        return new BaseResponse<>(HttpCode.SUCCESS, pipelineSetting);
    }

    @CompanyAdminPermission
    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @ApiOperation("自动清理设置接口")
    @PostMapping
    public BaseResponse<?> setPipelineSetting(@RequestBody @Valid PipelineSettingPayload payload) {
        PipelineSetting pipelineSetting = pipelineSettingService.saveOrUpdateSetting(authUtil.getCompanyId(), payload);
        return new BaseResponse<>(HttpCode.SUCCESS, pipelineSetting);
    }

}

