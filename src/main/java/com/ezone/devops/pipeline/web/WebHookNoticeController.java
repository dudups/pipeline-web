package com.ezone.devops.pipeline.web;

import com.ezone.devops.pipeline.annotations.WebHookPermission;
import com.ezone.devops.pipeline.context.RepoContext;
import com.ezone.devops.pipeline.service.WebHookNoticeService;
import com.ezone.devops.pipeline.web.request.WebHookNoticePayload;
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
import java.util.List;

@Slf4j
@Api(tags = "webHook通知设置")
@Validated
@RestController
@RequestMapping("/webhook_notices")
public class WebHookNoticeController {

    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private WebHookNoticeService webHookNoticeService;

    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @ApiOperation("查询webhook的详情")
    @GetMapping("/{hookId}")
    public BaseResponse<?> get(@PathVariable Long hookId) {
        Long companyId = authUtil.getCompanyId();
        List<WebHookNoticePayload> notices = webHookNoticeService.getWebHookNotice(companyId, hookId);
        return new BaseResponse<>(HttpCode.SUCCESS, notices);
    }

    @WebHookPermission(repoName = "#payload.repoName")
    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @ApiOperation("创建或更新webhook的通知")
    @PostMapping("/{hookId}")
    public BaseResponse<?> create(@PathVariable Long hookId,
                                  @RequestBody @Valid WebHookNoticePayload payload) {
        webHookNoticeService.createOrUpdateWebHookNotice(hookId, payload.getRepoName(), payload);
        return new BaseResponse<>(HttpCode.SUCCESS);
    }

    @WebHookPermission
    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @ApiOperation("删除webhook的通知")
    @DeleteMapping("/{hookId}")
    public BaseResponse<?> delete(@PathVariable Long hookId,
                                  @RequestParam String repoName) {
        webHookNoticeService.deleteWebHookNotice(RepoContext.get(), hookId);
        return new BaseResponse<>(HttpCode.SUCCESS);
    }

}

