package com.ezone.devops.pipeline.web;

import com.ezone.devops.pipeline.annotations.RepoPermission;
import com.ezone.devops.pipeline.common.ScmTriggerType;
import com.ezone.devops.pipeline.context.RepoContext;
import com.ezone.devops.pipeline.enums.RepoPermissionType;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.model.StageRecord;
import com.ezone.devops.pipeline.service.PipelineRecordService;
import com.ezone.devops.pipeline.service.StageRecordService;
import com.ezone.devops.pipeline.web.response.PipelineRecordResponse;
import com.ezone.devops.plugins.job.enums.VersionType;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags = "pipeline级别构建api")
@RestController
@RequestMapping("/build")
public class PipelineBuildOldController {

    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private StageRecordService stageRecordService;
    @Autowired
    private PipelineRecordService pipelineRecordService;

    @RepoPermission(requiredPermission = RepoPermissionType.PIPELINE_VIEW)
    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @ApiOperation("查询代码库下有权限的流水线记录")
    @GetMapping
    public BaseResponse<?> pipelineBuildList(@RequestParam String repoName,
                                             @RequestParam(required = false) Long pipelineId,
                                             @RequestParam(required = false) ScmTriggerType scmTriggerType,
                                             @RequestParam(required = false) String externalName,
                                             @RequestParam(required = false) String externalKey,
                                             @RequestParam(required = false) String commit,
                                             @RequestParam(required = false) VersionType versionType,
                                             @RequestParam(required = false) String version,
                                             @RequestParam(defaultValue = "1") @Min(1) int pageNumber,
                                             @RequestParam(defaultValue = "10") @Min(1) @Max(30) int pageSize,
                                             HttpServletResponse response) {
        PageResult<List<PipelineRecord>> pageResult = pipelineRecordService.getPipelineRecords(RepoContext.get(),
                authUtil.getUsername(), pipelineId, scmTriggerType, externalName, externalKey, commit, versionType, version, pageNumber, pageSize);
        if (pageResult == null) {
            response.setHeader(PageResult.PAGE_TOTAL_COUNT, String.valueOf(0));
            return new BaseResponse<>(HttpCode.SUCCESS);
        }
        response.setHeader(PageResult.PAGE_TOTAL_COUNT, String.valueOf(pageResult.getTotal()));
        return new BaseResponse<>(HttpCode.SUCCESS, convert(pageResult.getItems()));
    }

    private List<PipelineRecordResponse> convert(List<PipelineRecord> pipelineRecords) {
        List<StageRecord> stageRecords = stageRecordService.getStageRecordByPipelineRecords(pipelineRecords);
        Map<Long, List<StageRecord>> groupStageBuilds = groupStageRecord(stageRecords);
        List<PipelineRecordResponse> result = Lists.newArrayList();
        for (PipelineRecord pipelineRecord : pipelineRecords) {
            result.add(new PipelineRecordResponse(pipelineRecord, groupStageBuilds.get(pipelineRecord.getId())));
        }
        return result;
    }

    private Map<Long, List<StageRecord>> groupStageRecord(List<StageRecord> stageRecords) {
        Map<Long, List<StageRecord>> relations = Maps.newHashMap();
        if (CollectionUtils.isEmpty(stageRecords)) {
            return relations;
        }
        for (StageRecord stageRecord : stageRecords) {
            Long pipelineBuildId = stageRecord.getPipelineRecordId();
            if (relations.containsKey(pipelineBuildId)) {
                relations.get(pipelineBuildId).add(stageRecord);
            } else {
                relations.put(pipelineBuildId, Lists.newArrayList(stageRecord));
            }
        }
        return relations;
    }
}

