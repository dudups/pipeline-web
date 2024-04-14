package com.ezone.devops.pipeline.web;

import com.ezone.devops.pipeline.enums.PipelinePermissionType;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.service.PipelinePermissionService;
import com.ezone.devops.pipeline.service.PipelineService;
import com.ezone.devops.pipeline.service.RepoService;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.pipeline.web.request.PipelinePermissionPayload;
import com.ezone.ezbase.iam.bean.constant.CookieConstant;
import com.ezone.ezbase.iam.service.AuthUtil;
import com.ezone.galaxy.framework.common.bean.BaseException;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.bean.HttpCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "流水权限配置api")
@Slf4j
@RestController
@RequestMapping("/pipelines/{pipelineId}/permissions")
public class PipelinePermissionController {

    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private PipelineService pipelineService;
    @Autowired
    private RepoService repoService;
    @Autowired
    private PipelinePermissionService pipelinePermissionService;

    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @GetMapping("/has_permission")
    public BaseResponse<?> getPipelinePermission(@PathVariable("pipelineId") Long pipelineId,
                                                 @RequestParam(defaultValue = "PIPELINE_READER") PipelinePermissionType pipelinePermissionType) {
        Pipeline pipeline = pipelineService.getByIdIfPresent(pipelineId);
        RepoVo repoVo = repoService.getByRepoKey(pipeline.getCompanyId(), pipeline.getRepoKey());
        boolean hasPermission = pipelinePermissionService.hasPermission(repoVo, pipeline, pipelinePermissionType, authUtil.getUsername());
        return new BaseResponse<>(HttpCode.SUCCESS, hasPermission);
    }

    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @GetMapping
    public BaseResponse<?> getPipelinePermission(@PathVariable Long pipelineId) {
        Pipeline pipeline = checkPermission(pipelineId, PipelinePermissionType.PIPELINE_READER);
        PipelinePermissionPayload payload = pipelinePermissionService.getDetails(pipeline, authUtil.getUsername());
        return new BaseResponse<>(HttpCode.SUCCESS, payload);
    }


    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @PutMapping
    public BaseResponse<?> updatePipelinePermission(@PathVariable Long pipelineId,
                                                    @RequestBody @Validated PipelinePermissionPayload payload) {
        Pipeline pipeline = checkPermission(pipelineId, PipelinePermissionType.PIPELINE_MAINTAINER);
        boolean result = pipelinePermissionService.updatePermission(pipeline, payload);
        return new BaseResponse<>(HttpCode.SUCCESS, result);
    }

    private Pipeline checkPermission(Long pipelineId, PipelinePermissionType pipelinePermissionType) {
        Pipeline pipeline = pipelineService.getByIdIfPresent(pipelineId);
        String username = authUtil.getUsername();
        RepoVo repoVo = repoService.getByRepoKey(pipeline.getCompanyId(), pipeline.getRepoKey());
        boolean hasPermission = pipelinePermissionService.hasPermission(repoVo, pipeline, pipelinePermissionType, username);
        if (!hasPermission) {
            throw new BaseException(HttpStatus.FORBIDDEN.value(), "没有此条流水线的管理权限");
        }

        return pipeline;
    }
}

