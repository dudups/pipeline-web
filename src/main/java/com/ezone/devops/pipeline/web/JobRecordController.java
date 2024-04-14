package com.ezone.devops.pipeline.web;

import com.ezone.devops.pipeline.annotations.JobPermission;
import com.ezone.devops.pipeline.clients.CiLogClient;
import com.ezone.devops.pipeline.context.JobRecordContext;
import com.ezone.devops.pipeline.enums.PipelinePermissionType;
import com.ezone.devops.pipeline.event.service.JobRecordEventService;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.web.request.OperationPayload;
import com.ezone.ezbase.iam.bean.constant.CookieConstant;
import com.ezone.ezbase.iam.service.AuthUtil;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.bean.HttpCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;

@Api(tags = "job级别构建api")
@Validated
@RestController
@RequestMapping("/pipelines/{pipelineId}/jobs")
public class JobRecordController {

    private static final String LOG_SUFFIX = ".log";
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private CiLogClient ciLogClient;
    @Autowired
    private JobRecordEventService jobRecordEventService;


    @JobPermission(requiredPermission = PipelinePermissionType.PIPELINE_OPERATOR)
    @ApiOperation("job手动执行、重新执行、取消执行")
    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @PutMapping("/{jobId}")
    public BaseResponse<?> jobOperator(@PathVariable Long pipelineId,
                                       @PathVariable Long jobId,
                                       @RequestBody OperationPayload payload) {
        jobRecordEventService.jobOperator(JobRecordContext.get(), authUtil.getUsername(),
                payload.getOperationType(), payload.getParams());
        return new BaseResponse<>(HttpCode.SUCCESS);
    }

    @JobPermission(requiredPermission = PipelinePermissionType.PIPELINE_READER)
    @ApiOperation("查看执行的日志")
    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @GetMapping("/{jobId}/logs")
    public BaseResponse<?> getExecuteLog(@PathVariable Long pipelineId,
                                         @PathVariable Long jobId) throws IOException {
        JobRecord jobRecord = JobRecordContext.get();
        String logName = jobRecord.getLogName();
        if (StringUtils.isEmpty(logName)) {
            return new BaseResponse<>(HttpCode.SUCCESS);
        }
        InputStream inputStream = ciLogClient.readLogStream(logName, false);
        if (inputStream == null) {
            return new BaseResponse<>(HttpCode.SUCCESS);
        }
        return new BaseResponse<>(HttpCode.SUCCESS, StreamUtils.copyToString(inputStream, Charset.defaultCharset()));
    }

    @JobPermission(requiredPermission = PipelinePermissionType.PIPELINE_READER)
    @ApiOperation(value = "下载日志")
    @ApiImplicitParam(name = CookieConstant.COMPANY_HEADER, required = true, dataType = "String", paramType = "header")
    @GetMapping("/{jobId}/log_download")
    public void downloadLog(@PathVariable Long pipelineId,
                            @PathVariable Long jobId,
                            HttpServletResponse response) throws IOException {
        JobRecord jobRecord = JobRecordContext.get();
        String logName = jobRecord.getLogName();
        if (StringUtils.isEmpty(logName)) {
            return;
        }
        InputStream inputStream = ciLogClient.readLogStream(logName, true);
        String fileName = URLEncoder.encode(logName + LOG_SUFFIX, "UTF-8");
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
        ServletOutputStream outputStream = response.getOutputStream();
        if (inputStream != null) {
            StreamUtils.copy(inputStream, outputStream);
        }
    }

}

