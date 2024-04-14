package com.ezone.devops.pipeline.web;

import com.ezone.devops.ezcode.sdk.bean.model.InternalCommit;
import com.ezone.devops.ezcode.sdk.service.InternalCommitService;
import com.ezone.devops.pipeline.annotations.PipelinePermission;
import com.ezone.devops.pipeline.annotations.PipelineRecordPermission;
import com.ezone.devops.pipeline.common.ScmTriggerType;
import com.ezone.devops.pipeline.context.PipelineContext;
import com.ezone.devops.pipeline.context.PipelineRecordContext;
import com.ezone.devops.pipeline.context.RepoContext;
import com.ezone.devops.pipeline.enums.PipelinePermissionType;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.model.ReleaseVersion;
import com.ezone.devops.pipeline.model.StageRecord;
import com.ezone.devops.pipeline.service.*;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.pipeline.web.request.RuntimeVariablePayload;
import com.ezone.devops.pipeline.web.response.PipelineRecordResponse;
import com.ezone.devops.pipeline.web.response.TriggerModeResponse;
import com.ezone.devops.plugins.job.enums.VersionType;
import com.ezone.devops.plugins.job.release.model.ArtifactReleaseBuild;
import com.ezone.devops.plugins.job.release.service.ArtifactReleaseBuildService;
import com.ezone.devops.plugins.service.ArtifactInfoService;
import com.ezone.devops.plugins.vo.VersionInfo;
import com.ezone.ezbase.iam.bean.constant.CookieConstant;
import com.ezone.ezbase.iam.service.AuthUtil;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.bean.HttpCode;
import com.ezone.galaxy.framework.common.bean.PageResult;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags = "pipeline级别构建api")
@RestController
@RequestMapping("/pipelines/{pipelineId}/builds")
public class PipelineRecordController {

    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private PipelineRecordService pipelineRecordService;
    @Autowired
    private StageRecordService stageRecordService;
    @Autowired
    private JobRecordService jobRecordService;
    @Autowired
    private RuntimeVariableService runtimeVariableService;
    @Autowired
    private ArtifactInfoService artifactInfoService;
    @Autowired
    private InternalCommitService commitService;
    @Autowired
    private VersionService versionService;
    @Autowired
    private ArtifactReleaseBuildService artifactReleaseBuildService;

    @PipelineRecordPermission(requiredPermission = PipelinePermissionType.PIPELINE_READER)
    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @ApiOperation("按照流水线构建id查询记录的详情")
    @GetMapping("/{recordId}")
    public BaseResponse<?> pipelineRecords(@PathVariable Long pipelineId,
                                           @PathVariable Long recordId) {
        PipelineRecord pipelineRecord = PipelineRecordContext.get();
        List<StageRecord> stageRecords = stageRecordService.getOrderedStageRecordByPipelineRecord(pipelineRecord);
        return new BaseResponse<>(HttpCode.SUCCESS, new PipelineRecordResponse(pipelineRecord, stageRecords));
    }

    @PipelineRecordPermission(requiredPermission = PipelinePermissionType.PIPELINE_READER)
    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "设置流水线的运行时环境变量")
    @PostMapping("/{recordId}/variables")
    public BaseResponse<?> mergeVariables(@PathVariable Long pipelineId,
                                          @PathVariable Long recordId,
                                          @RequestBody RuntimeVariablePayload payload) {
        return new BaseResponse<>(HttpCode.SUCCESS, runtimeVariableService.mergeRuntimeVariables(PipelineContext.get(), PipelineRecordContext.get(), payload.getVariables()));
    }

    @PipelineRecordPermission(requiredPermission = PipelinePermissionType.PIPELINE_READER)
    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @ApiOperation("按照记录id查询job的详情")
    @GetMapping("/{recordId}/jobs")
    public BaseResponse<?> jobs(@PathVariable Long pipelineId,
                                @PathVariable Long recordId) {
        return new BaseResponse<>(HttpCode.SUCCESS, jobRecordService.getJobRecordGroups(PipelineContext.get(), PipelineRecordContext.get()));
    }

    @PipelineRecordPermission(requiredPermission = PipelinePermissionType.PIPELINE_READER)
    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "查询流水线下的制品列表")
    @GetMapping("/{recordId}/artifacts")
    public BaseResponse<?> getArtifacts(@PathVariable Long pipelineId,
                                        @PathVariable Long recordId) {
        return new BaseResponse<>(HttpCode.SUCCESS, artifactInfoService.getAllByPipelineRecord(PipelineRecordContext.get()));
    }

    @PipelineRecordPermission(requiredPermission = PipelinePermissionType.PIPELINE_READER)
    @ApiOperation(value = "查询流水线构建记录关联的卡片列表")
    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @GetMapping("/{recordId}/cards")
    public BaseResponse<?> getBuildRelateCards(@PathVariable Long pipelineId,
                                               @PathVariable Long recordId,
                                               @RequestParam("fields") String[] fields) {
        return pipelineRecordService.getPipelineBuildRelateCards(RepoContext.get(), PipelineRecordContext.get(), fields);
    }

    @PipelineRecordPermission(requiredPermission = PipelinePermissionType.PIPELINE_READER)
    @ApiOperation("查询流水线构建记录触发事件详情")
    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @GetMapping("/{recordId}/trigger_mode")
    public BaseResponse<?> getBuildTriggerModeDetail(@PathVariable Long pipelineId,
                                                     @PathVariable Long recordId) {
        PipelineRecord pipelineRecord = PipelineRecordContext.get();
        Object triggerModeDetail = pipelineRecordService.getTriggerModeDetail(pipelineRecord);
        return new BaseResponse<>(HttpCode.SUCCESS, TriggerModeResponse.convert(pipelineRecord, triggerModeDetail));
    }

    @PipelineRecordPermission(requiredPermission = PipelinePermissionType.PIPELINE_READER)
    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @ApiOperation("查询commit信息")
    @GetMapping("/{recordId}/commits/{commitId}")
    public BaseResponse<?> getCommitMessage(@PathVariable Long pipelineId,
                                            @PathVariable Long recordId,
                                            @PathVariable String commitId) {
        RepoVo repo = RepoContext.get();
        InternalCommit commit = commitService.getCommit(repo.getCompanyId(), repo.getRepoKey(), commitId);
        return new BaseResponse<>(HttpCode.SUCCESS, commit);
    }

    @PipelineRecordPermission(requiredPermission = PipelinePermissionType.PIPELINE_READER)
    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "获取推荐版本")
    @GetMapping("/{recordId}/recommend_version")
    public BaseResponse<?> getRecommendVersion(@PathVariable Long pipelineId,
                                               @PathVariable Long recordId) {
        RepoVo repo = RepoContext.get();
        ReleaseVersion releaseVersion = versionService.getLastReleaseVersion(repo);

        VersionInfo versionInfo = new VersionInfo();
        versionInfo.setLastVersion(releaseVersion.getVersion());

        PipelineRecord pipelineRecord = PipelineRecordContext.get();
        if (StringUtils.isNotBlank(pipelineRecord.getReleaseVersion())) {
            versionInfo.setVersion(pipelineRecord.getReleaseVersion());
            return new BaseResponse<>(HttpCode.SUCCESS, versionInfo);
        }

        ArtifactReleaseBuild artifactReleaseBuild = artifactReleaseBuildService.getByPipelineBuildId(recordId);
        if (artifactReleaseBuild != null && StringUtils.isNotBlank(artifactReleaseBuild.getVersion())) {
            versionInfo.setMessage(artifactReleaseBuild.getMessage());
            versionInfo.setVersion(artifactReleaseBuild.getVersion());
            return new BaseResponse<>(HttpCode.SUCCESS, versionInfo);
        }

        return new BaseResponse<>(HttpCode.SUCCESS, versionInfo);
    }


    @PipelinePermission(requiredPermission = PipelinePermissionType.PIPELINE_READER)
    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @ApiOperation("查询代码库下流水线构建记录")
    @GetMapping
    public BaseResponse<?> pipelineBuilds(@PathVariable Long pipelineId,
                                          @RequestParam String repoName,
                                          @RequestParam(required = false) ScmTriggerType scmTriggerType,
                                          @RequestParam(required = false) String externalName,
                                          @RequestParam(required = false) String externalKey,
                                          @RequestParam(required = false) String commit,
                                          @RequestParam(required = false) VersionType versionType,
                                          @RequestParam(required = false) String version,
                                          @RequestParam(defaultValue = "1") @Min(1) int pageNumber,
                                          @RequestParam(defaultValue = "10") @Min(1) @Max(30) int pageSize,
                                          HttpServletResponse response) {
        RepoVo repo = RepoContext.get();
        PageResult<List<PipelineRecord>> pageResult = pipelineRecordService.getPipelineRecords(repo, authUtil.getUsername(), pipelineId, scmTriggerType, externalName, externalKey, commit, versionType, version, pageNumber, pageSize);
        if (pageResult == null) {
            response.setHeader(PageResult.PAGE_TOTAL_COUNT, String.valueOf(0));
            return new BaseResponse<>(HttpCode.SUCCESS);
        }
        response.setHeader(PageResult.PAGE_TOTAL_COUNT, String.valueOf(pageResult.getTotal()));
        return new BaseResponse<>(HttpCode.SUCCESS, convert(pageResult.getItems()));
    }

    private List<PipelineRecordResponse> convert(List<PipelineRecord> pipelineRecords) {
        List<StageRecord> stageRecords = stageRecordService.getStageRecordByPipelineRecords(pipelineRecords);
        Map<Long, List<StageRecord>> groupStageBuilds = groupStageBuild(stageRecords);
        List<PipelineRecordResponse> result = Lists.newArrayList();
        for (PipelineRecord pipelineRecord : pipelineRecords) {
            result.add(new PipelineRecordResponse(pipelineRecord, groupStageBuilds.get(pipelineRecord.getId())));
        }
        return result;
    }

    private Map<Long, List<StageRecord>> groupStageBuild(List<StageRecord> stageRecords) {
        Map<Long, List<StageRecord>> relations = Maps.newHashMap();
        if (CollectionUtils.isEmpty(stageRecords)) {
            return relations;
        }
        for (StageRecord stageBuild : stageRecords) {
            Long pipelineBuildId = stageBuild.getPipelineRecordId();
            if (relations.containsKey(pipelineBuildId)) {
                relations.get(pipelineBuildId).add(stageBuild);
            } else {
                relations.put(pipelineBuildId, Lists.newArrayList(stageBuild));
            }
        }
        return relations;
    }
}

