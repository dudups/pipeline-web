package com.ezone.devops.pipeline.web;

import com.ezone.devops.pipeline.annotations.PipelinePermission;
import com.ezone.devops.pipeline.annotations.RepoPermission;
import com.ezone.devops.pipeline.clients.EzProjectClient;
import com.ezone.devops.pipeline.clients.request.EzProjectPayload;
import com.ezone.devops.pipeline.clients.util.DateUtil;
import com.ezone.devops.pipeline.common.TriggerMode;
import com.ezone.devops.pipeline.context.PipelineContext;
import com.ezone.devops.pipeline.context.RepoContext;
import com.ezone.devops.pipeline.enums.PipelinePermissionType;
import com.ezone.devops.pipeline.enums.RepoPermissionType;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.model.Stage;
import com.ezone.devops.pipeline.service.PipelineService;
import com.ezone.devops.pipeline.service.RepoService;
import com.ezone.devops.pipeline.service.StageService;
import com.ezone.devops.pipeline.service.TriggerPipelineService;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.pipeline.web.request.PipelinePayload;
import com.ezone.devops.pipeline.web.request.TriggerPipelinePayload;
import com.ezone.devops.pipeline.web.request.VariablePair;
import com.ezone.devops.pipeline.web.response.PipelineConfigResponse;
import com.ezone.ezbase.iam.bean.constant.CookieConstant;
import com.ezone.ezbase.iam.service.AuthUtil;
import com.ezone.galaxy.framework.common.bean.BaseException;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.bean.HttpCode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.omg.PortableInterceptor.SUCCESSFUL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Api(tags = "流水线配置api")
@Slf4j
@Validated
@RestController
@RequestMapping("/pipelines")
public class PipelineController {

    private static final Pattern BUILD_ENV_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]*$");

    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private RepoService repoService;
    @Autowired
    private PipelineService pipelineService;
    @Autowired
    private StageService stageService;
    @Autowired
    private TriggerPipelineService triggerPipelineService;

    @Autowired
    private EzProjectClient ezProjectClient;


    @RepoPermission(requiredPermission = RepoPermissionType.PIPELINE_VIEW)
    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @ApiOperation("查询是否有同名流水线")
    @GetMapping("/exist")
    public BaseResponse<?> hasSameNamePipeline(@RequestParam @NotBlank String repoName,
                                               @RequestParam @NotBlank String pipelineName) {
        Pipeline pipeline = pipelineService.getPipeline(RepoContext.get(), pipelineName);
        return new BaseResponse<>(HttpCode.SUCCESS, pipeline != null);
    }

    @RepoPermission(repoName = "#payload.repoName", requiredPermission = RepoPermissionType.PIPELINE_MANAGE)
    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @ApiOperation("保存流水线")
    @PostMapping
    public BaseResponse<?> savePipelineConfig(@RequestBody @Valid PipelinePayload payload) {
        Pipeline pipeline = pipelineService.savePipeline(payload.getRepoName(), payload, authUtil.getUsername());
        return new BaseResponse<>(HttpCode.SUCCESS, pipeline);
    }

    @PipelinePermission(pipelineId = "#id", requiredPermission = PipelinePermissionType.PIPELINE_MAINTAINER)
    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @ApiOperation("更新流水线")
    @PutMapping("/{id}")
    public BaseResponse<?> updatePipelineConfig(@PathVariable Long id,
                                                @RequestBody @Valid PipelinePayload payload) {
        Pipeline pipeline = pipelineService.updatePipeline(RepoContext.get(), PipelineContext.get(), payload, authUtil.getUsername());
        return new BaseResponse<>(HttpCode.SUCCESS, pipeline);
    }

    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "查询代码库下有权限的的流水线", notes = "withStageDetail为true的会返回stage的信息")
    @GetMapping
    public BaseResponse<?> getAuthorizedPipelines(@RequestParam String repoName,
                                                  @RequestParam(required = false) String pipelineName,
                                                  @RequestParam(defaultValue = "false") boolean withStageDetail) {
        RepoVo repo = repoService.getRepoByNameIfPresent(authUtil.getCompanyId(), repoName);
        List<Pipeline> authorizedPipelines = pipelineService.getAuthorizedPipeline(repo, authUtil.getUsername(), pipelineName);
        if (CollectionUtils.isEmpty(authorizedPipelines)) {
            return new BaseResponse<>(HttpCode.SUCCESS);
        }

        TreeMap<Long, Pipeline> pipelineMap = Maps.newTreeMap();
        for (Pipeline authorizedPipeline : authorizedPipelines) {
            pipelineMap.put(authorizedPipeline.getId(), authorizedPipeline);
        }

        List<PipelineConfigResponse> pipelineConfigResponses = Lists.newArrayListWithCapacity(pipelineMap.size());
        if (withStageDetail) {
            Map<Long, List<Stage>> stageGroups = stageService.getStageGroup(pipelineMap.keySet());
            for (Pipeline pipeline : pipelineMap.values()) {
                if (MapUtils.isNotEmpty(stageGroups)) {
                    PipelineConfigResponse pipelineConfigResponse = new PipelineConfigResponse(pipeline, stageGroups.get(pipeline.getId()));
                    pipelineConfigResponses.add(pipelineConfigResponse);
                }
            }
        } else {
            for (Pipeline pipeline : pipelineMap.values()) {
                PipelineConfigResponse pipelineConfigResponse = new PipelineConfigResponse(pipeline);
                pipelineConfigResponses.add(pipelineConfigResponse);
            }
        }

        return new BaseResponse<>(HttpCode.SUCCESS, pipelineConfigResponses);
    }

    @PipelinePermission(pipelineId = "#id", requiredPermission = PipelinePermissionType.PIPELINE_READER)
    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @ApiOperation("查询流水线的配置信息")
    @GetMapping("/{id}")
    public BaseResponse<?> getPipeline(@PathVariable Long id) {
        PipelinePayload pipelinePayload = pipelineService.getPipelinePayload(id);
        return new BaseResponse<>(HttpCode.SUCCESS, pipelinePayload);
    }

    @PipelinePermission(pipelineId = "#id", requiredPermission = PipelinePermissionType.PIPELINE_ADMIN)
    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @ApiOperation("删除流水线")
    @DeleteMapping("/{id}")
    public BaseResponse<?> deletePipelineConfig(@PathVariable Long id) {
        return new BaseResponse<>(HttpCode.SUCCESS, pipelineService.deletePipeline(PipelineContext.get()));
    }

    @PipelinePermission(pipelineId = "#id", requiredPermission = PipelinePermissionType.PIPELINE_OPERATOR)
    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @ApiOperation("触发流水线")
    @PostMapping("/{id}/manual")
    public BaseResponse<?> triggerNewPipeline(@PathVariable Long id,
                                              @RequestBody @Valid TriggerPipelinePayload payload) {
        Set<VariablePair> variables = payload.getVariables();
        if (CollectionUtils.isNotEmpty(variables)) {
            validateVariables(variables);
        }

        Pipeline pipeline = PipelineContext.get();
        PipelineRecord pipelineRecord = triggerPipelineService
                .triggerPipeline(pipeline, payload, authUtil.getUsername(), TriggerMode.MANUAL, StringUtils.EMPTY);
        return new BaseResponse<>(HttpCode.SUCCESS, pipelineRecord);
    }

    @ApiOperation("测试")
    @PostMapping("/test")
    public BaseResponse<?> test() {
        System.out.println(new Date());
        EzProjectPayload ezProjectPayload = EzProjectPayload.builder()
                .clusterKey("111")
                .companyId(1L)
                .nameSpace("22")
                .repoId(222L)
                .repoName("test")
                .build();
        BaseResponse<String> baseResponse =  ezProjectClient.isPassPipeline(ezProjectPayload);
        System.out.println(new Date());
        return new BaseResponse<>(HttpCode.SUCCESS,baseResponse);
    }


    private void validateVariables(Collection<VariablePair> variables) {
        for (VariablePair variable : variables) {
            String key = variable.getEnvKey();
            Matcher matcher = BUILD_ENV_PATTERN.matcher(key);
            if (!matcher.matches()) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "用户自定义变量不能以“SYS_”开头，只支持“^[a-zA-Z][a-zA-Z0-9_]*$”");
            }
        }
    }
}

