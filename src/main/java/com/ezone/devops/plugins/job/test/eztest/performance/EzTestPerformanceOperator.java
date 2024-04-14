package com.ezone.devops.plugins.job.test.eztest.performance;

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
import com.ezone.devops.plugins.job.test.eztest.performance.bean.EzTestPerformanceBuildBean;
import com.ezone.devops.plugins.job.test.eztest.performance.bean.EzTestPerformanceConfigBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class EzTestPerformanceOperator extends AbstractPluginOperator implements PluginOperator {

    @Autowired
    private EzTestPerformanceDataOperator ezTestPerformanceDataOperator;
    @Autowired
    private EzTestClient ezTestClient;

    @Override
    public boolean execute(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);
        EzTestPerformanceConfigBean ezTestPerformanceConfig = ezTestPerformanceDataOperator.getRealJob(jobRecord.getPluginId());

        if (!ezTestPerformanceConfig.isUseCustomCluster()) {
            if (pipeline.isUseDefaultCluster()) {
                ezTestPerformanceConfig.setResourceType(pipeline.getResourceType());
                ezTestPerformanceConfig.setResourceName(pipeline.getClusterName());
            }
        }

        EzTestResponse response = ezTestClient.startPerformanceTest(pipeline, jobRecord, ezTestPerformanceConfig, envs);
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

        EzTestPerformanceBuildBean performanceBuildBean = ezTestPerformanceDataOperator.getRealJobRecord(jobRecord.getPluginRecordId());
        performanceBuildBean.setPlanId(ezTestResult.getPlanId());
        performanceBuildBean.setPlanSeqNum(ezTestResult.getPlanSeqNum());
        performanceBuildBean.setSpaceKey(ezTestResult.getSpaceKey());
        ezTestPerformanceDataOperator.updateRealJobRecord(jobRecord.getPluginRecordId(), JSONObject.parseObject(JSONObject.toJSONString(performanceBuildBean)));
        return true;
    }

    @Override
    public void cancel(PipelineRecord pipelineRecord, JobRecord jobRecord, String triggerUser) {
        ezTestClient.stopPerformanceTest(jobRecord.getExternalJobId());
    }

    @Override
    public void jobAbortCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data) {
        super.jobAbortCallback(pipelineRecord, jobRecord, message, data);
        ezTestClient.stopPerformanceTest(jobRecord.getExternalJobId());
    }
}