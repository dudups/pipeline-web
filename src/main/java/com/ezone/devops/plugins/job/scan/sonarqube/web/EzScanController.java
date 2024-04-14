package com.ezone.devops.plugins.job.scan.sonarqube.web;

import com.ezone.devops.pipeline.clients.EzScanClient;
import com.ezone.devops.pipeline.enums.PipelinePermissionType;
import com.ezone.devops.pipeline.exception.NoPermissionOperateException;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.job.scan.ezscan.EzScanDataOperator;
import com.ezone.devops.plugins.job.scan.ezscan.bean.EzScanBuildBean;
import com.ezone.devops.plugins.web.PluginBaseController;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Valid
@Api(tags = "内置扫描api")
@RestController
@RequestMapping("/plugins/jobs/{jobId}/ezscan")
public class EzScanController extends PluginBaseController {

    @Autowired
    private EzScanClient ezScanClient;
    @Autowired
    private EzScanDataOperator ezScanDataOperator;

    @ApiOperation(value = "查询扫描日志")
    @GetMapping("/scan_log")
    public BaseResponse<?> scanLog(@PathVariable Long jobId) {
        JobRecord jobRecord = jobRecordService.getByIdIfPresent(jobId);
        Pipeline pipeline = pipelineService.getByIdIfPresent(jobRecord.getPipelineId());
        RepoVo repoVo = repoService.getByRepoKeyIfPresent(pipeline.getCompanyId(), pipeline.getRepoKey());
        boolean permission = pipelinePermissionService.hasPermission(repoVo, pipeline, PipelinePermissionType.PIPELINE_READER, authUtil.getUsername());
        if (!permission) {
            throw new NoPermissionOperateException();
        }
        EzScanBuildBean ezScanBuildBean = ezScanDataOperator.getRealJobRecord(jobRecord.getPluginRecordId());
        return ezScanClient.queryScanLog(ezScanBuildBean.getTaskId(), ezScanBuildBean.getProjectId());
    }
}

