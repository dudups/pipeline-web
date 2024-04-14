package com.ezone.devops.plugins.job.scan.ezscan;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.ezcode.sdk.bean.model.PipelineScanBaseCommit;
import com.ezone.devops.ezcode.sdk.service.InternalCommitService;
import com.ezone.devops.pipeline.clients.EzScanClient;
import com.ezone.devops.pipeline.clients.request.EzScanCancelPayload;
import com.ezone.devops.pipeline.clients.request.EzScanPayload;
import com.ezone.devops.pipeline.clients.response.EzScanResponse;
import com.ezone.devops.pipeline.common.ScmTriggerType;
import com.ezone.devops.pipeline.enums.GlobalVariableType;
import com.ezone.devops.pipeline.enums.ResourceType;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.enums.ScanLevel;
import com.ezone.devops.plugins.job.AbstractPluginOperator;
import com.ezone.devops.plugins.job.PluginOperator;
import com.ezone.devops.plugins.job.scan.ezscan.bean.EzScanBuildBean;
import com.ezone.devops.plugins.job.scan.ezscan.bean.EzScanConfigBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class EzScanOperator extends AbstractPluginOperator implements PluginOperator {

    @Autowired
    private EzScanDataOperator ezScanDataOperator;
    @Autowired
    private EzScanClient ezScanClient;
    @Autowired
    private InternalCommitService internalCommitService;

    @Override
    public boolean execute(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        String triggerUser = jobRecord.getTriggerUser();
        EzScanConfigBean ezScanConfig = ezScanDataOperator.getRealJob(jobRecord.getPluginId());

        String clusterName;
        if (ezScanConfig.isUseSelfCiPool()) {
            clusterName = ezScanConfig.getClusterName();
        } else {
            if (pipeline.isUseDefaultCluster()) {
                clusterName = systemConfig.getDefaultClusterName();
            } else {
                clusterName = pipeline.getClusterName();
            }
        }

        Map<String, String> envs = runtimeVariableService.getAllVariables(repo, pipeline, pipelineRecord, jobRecord);
        log.info("start request ezcsacn config:[{}]", jobRecord);
        String companyName = envs.get(GlobalVariableType.SYS_COMPANY_NAME.getKey());
        String callbackUrl = getCallbackUrl(jobRecord);

        String baseCommit = null;
        if (ezScanConfig.isIncrementScan()) {
            PipelineScanBaseCommit pipelineScanBaseCommit = internalCommitService.getPipelineScanBaseCommit(repo.getCompanyId(),
                    repo.getRepoKey(), pipelineRecord.getTriggerMode().name(), pipelineRecord.getExternalKey(),
                    pipelineRecord.getCommitId(), !ezScanConfig.isIncrementScan());
            if (pipelineScanBaseCommit != null) {
                if (pipelineScanBaseCommit.shouldTriggerIncrementalScan()) {
                    baseCommit = pipelineScanBaseCommit.getCommitId();
                }
            }
        }

        ResourceType resourceType;
        if (ezScanConfig.isUseSelfCiPool()) {
            resourceType = ResourceType.HOST;
        } else {
            if (pipeline.isUseDefaultCluster()) {
                resourceType = ResourceType.KUBERNETES;
            } else {
                resourceType = pipeline.getResourceType();
            }
        }

        EzScanPayload ezScanPayload = new EzScanPayload()
                .setCompanyId(repo.getCompanyId())
                .setCompanyName(companyName)
                .setRepoId(pipelineRecord.getRepoKey())
                .setBaseCommit(baseCommit)
                .setCreator(triggerUser)
                .setFilterDirs(ezScanConfig.getFilterDirs())
                .setScanLevel(ezScanConfig.getScanLevel())
                .setRulesetId(ezScanConfig.getRulesetId())
                .setTimeoutSpan(pipeline.getJobTimeoutMinute())
                .setIsDotNetFramework(ezScanConfig.isDotNetFramework())
                .setIsXcodeApp(ezScanConfig.getIsXcodeApp())
                .setResourceType(resourceType)
                .setResourceName(clusterName)
                .setCallbackUrl(callbackUrl);

        if (pipelineRecord.getScmTriggerType() == ScmTriggerType.BRANCH || pipelineRecord.getScmTriggerType() == ScmTriggerType.COMMIT) {
            ezScanPayload.setBranch(pipelineRecord.getExternalName());
            ezScanPayload.setCommit(pipelineRecord.getCommitId());
        } else {
            ezScanPayload.setBranch(pipelineRecord.getExternalName());
        }

        EzScanResponse response = ezScanClient.triggerScan(ezScanPayload);
        if (response == null) {
            log.info("invoke ezscan error,response is null, job:[{}]", jobRecord);
            super.invokeDownStreamSystemFailCallback(pipelineRecord, jobRecord, "调用ezScan代码扫描服务失败");
            return false;
        }

        if (response.isError()) {
            log.info("invoke ezscan error, response:[{}]", response);
            super.noticeJobFailed(pipelineRecord, jobRecord, response.getMessage());
            return false;
        }

        String jsonString = JSONObject.toJSONString(response.getData());
        ezScanDataOperator.updateRealJobRecord(jobRecord.getPluginRecordId(), JSONObject.parseObject(jsonString));
        return true;
    }

    @Override
    public void jobStartCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message) {
        // 开始扫描
    }

    @Override
    public void jobSuccessCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data) {
        ezScanDataOperator.updateRealJobBuildWithFields(jobRecord.getPluginRecordId(), data);
        EzScanConfigBean ezScanConfigBean = ezScanDataOperator.getRealJob(jobRecord.getPluginId());
        EzScanBuildBean ezScanBuildBean = ezScanDataOperator.getRealJobRecord(jobRecord.getPluginRecordId());

        // 判断是否开启质量门禁
        if (ezScanConfigBean.isEnableQos()) {
            int count;
            ScanLevel scanLevel = ezScanConfigBean.getScanLevel();
            Integer blockCount = ezScanBuildBean.getBlock();
            Integer highCount = ezScanBuildBean.getHigh();
            Integer middleCount = ezScanBuildBean.getMiddle();
            Integer lowCount = ezScanBuildBean.getLow();
            switch (scanLevel) {
                case BLOCK: {
                    count = blockCount;
                    if (count > ezScanConfigBean.getQosCount()) {
                        jobFailedCallback(pipelineRecord, jobRecord, "问题数量超过质量门禁设置", data);
                    } else {
                        super.jobSuccessCallback(pipelineRecord, jobRecord, message, data);
                    }
                    break;
                }
                case HIGH: {
                    count = blockCount + highCount;
                    if (count > ezScanConfigBean.getQosCount()) {
                        jobFailedCallback(pipelineRecord, jobRecord, "问题数量超过质量门禁设置", data);
                    } else {
                        super.jobSuccessCallback(pipelineRecord, jobRecord, message, data);
                    }
                    break;
                }
                case MIDDLE: {
                    count = blockCount + highCount + middleCount;
                    if (count > ezScanConfigBean.getQosCount()) {
                        jobFailedCallback(pipelineRecord, jobRecord, "问题数量超过质量门禁设置", data);
                    } else {
                        super.jobSuccessCallback(pipelineRecord, jobRecord, message, data);
                    }
                    break;
                }
                default: {
                    count = blockCount + highCount + middleCount + lowCount;
                    if (count > ezScanConfigBean.getQosCount()) {
                        jobFailedCallback(pipelineRecord, jobRecord, "问题数量超过质量门禁设置", data);
                    } else {
                        super.jobSuccessCallback(pipelineRecord, jobRecord, message, data);
                    }
                }
            }
        } else {
            super.jobSuccessCallback(pipelineRecord, jobRecord, message, data);
        }
    }

    @Override
    public void cancel(PipelineRecord pipelineRecord, JobRecord jobRecord, String triggerUser) {
        EzScanBuildBean ezScanBuildBean = ezScanDataOperator.getRealJobRecord(jobRecord.getPluginRecordId());
        Pipeline pipeline = pipelineService.getByIdIfPresent(jobRecord.getPipelineId());
        String companyName = iamCenterService.getCompanyNameByCompanyId(pipeline.getCompanyId());

        EzScanCancelPayload payload = new EzScanCancelPayload();
        payload.setCompanyName(companyName);
        payload.setProjectId(ezScanBuildBean.getProjectId());
        payload.setUserName(triggerUser);

        ezScanClient.cancelScan(ezScanBuildBean.getTaskId(), payload);
    }

    @Override
    public void jobAbortCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, String message, JSONObject data) {
        super.jobAbortCallback(pipelineRecord, jobRecord, message, data);
    }
}