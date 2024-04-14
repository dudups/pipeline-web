package com.ezone.devops.pipeline.web;

import com.ezone.devops.pipeline.service.PipelineTemplateService;
import com.ezone.devops.pipeline.web.request.PipelineTemplatePayload;
import com.ezone.ezbase.iam.bean.constant.CookieConstant;
import com.ezone.ezbase.iam.service.AuthUtil;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.bean.HttpCode;
import com.ezone.galaxy.framework.common.bean.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Api(tags = "流水线模板配置api")
@Slf4j
@Validated
@RestController
@RequestMapping("/templates_ext")
public class PipelineTemplateExtController {

    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private PipelineTemplateService pipelineTemplateService;

    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @GetMapping()
    public BaseResponse<?> listTemplate(@RequestParam(required = false) String keyword,
                                        @RequestParam(defaultValue = "1") @Min(1) int pageNumber,
                                        @RequestParam(defaultValue = "10") @Min(1) @Max(30) int pageSize,
                                        HttpServletResponse response) {
        PageResult<List<PipelineTemplatePayload>> result = pipelineTemplateService.suggestTemplate(authUtil.getCompanyId(), keyword, pageNumber, pageSize);
        response.setHeader(PageResult.PAGE_TOTAL_COUNT, String.valueOf(result.getTotal()));
        return new BaseResponse<>(HttpCode.SUCCESS, result.getItems());
    }

    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @GetMapping("/{id}")
    public BaseResponse<?> getTemplate(@PathVariable Long id) {
        return new BaseResponse<>(HttpCode.SUCCESS, pipelineTemplateService.getTemplate(authUtil.getCompanyId(), id));
    }
}

