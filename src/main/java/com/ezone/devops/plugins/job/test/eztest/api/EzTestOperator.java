package com.ezone.devops.plugins.job.test.eztest.api;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.clients.EzTestClient;
import com.ezone.devops.pipeline.clients.response.EzTestResponse;
import com.ezone.devops.pipeline.clients.response.EzTestResult;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.job.AbstractPluginOperator;
import com.ezone.devops.plugins.job.PluginOperator;
import com.ezone.devops.plugins.job.test.eztest.api.model.EzTestBuild;
import com.ezone.devops.plugins.job.test.eztest.api.model.EzTestConfig;
import com.ezone.devops.plugins.job.test.eztest.api.service.EzTestBuildService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class EzTestOperator extends AbstractPluginOperator implements PluginOperator {

    @Autowired
    private EzTestDataOperator ezTestDataOperator;
    @Autowired
    private EzTestBuildService ezTestBuildService;
    @Autowired
    private EzTestClient ezTestClient;

    @Override
    public boolean execute(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);
        EzTestConfig ezTestConfig = ezTestDataOperator.getRealJob(jobRecord.getPluginId());
        EzTestResponse response = ezTestClient.startApiTest(pipeline, jobRecord, ezTestConfig, envs);
        if (response == null) {
            log.info("invoke eztest error, response is null, job:[{}]", jobRecord);
            super.invokeDownStreamSystemFailCallback(pipelineRecord, jobRecord, ezTestClient.getPlatformName());
            return false;
        }

        if (response.isError()) {
            log.info("invoke eztest error, response:[{}]", response);
            super.noticeJobFailed(pipelineRecord, jobRecord, response.getMessage());
            return false;
        }

        log.info("eztest response is {}", response);
        EzTestResult ezTestResult = response.getData();

        EzTestBuild ezTestBuild = ezTestBuildService.getById(jobRecord.getPluginRecordId());
        ezTestBuild.setPlanId(ezTestResult.getPlanId());
        ezTestBuild.setSpaceKey(ezTestResult.getSpaceKey());
        ezTestBuildService.updateBuild(ezTestBuild);
        return true;
    }

    @Override
    public void cancel(PipelineRecord pipelineRecord, JobRecord jobRecord, String triggerUser) {
        ezTestClient.stopApiTest(jobRecord.getExternalJobId());
    }

    @Override
    public void jobAbortCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data) {
        super.jobAbortCallback(pipelineRecord, jobRecord, message, data);
        ezTestClient.stopApiTest(jobRecord.getExternalJobId());
    }
}