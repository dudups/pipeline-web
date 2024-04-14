package com.ezone.devops.pipeline.service.impl;

import com.ezone.devops.pipeline.common.BuildStatus;
import com.ezone.devops.pipeline.common.JobEvent;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.service.PipelineHookService;
import com.ezone.devops.plugins.web.request.PluginCallbackPayload;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.util.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PipelineHookServiceImpl implements PipelineHookService {

    @Override
    public void noticeStart(PipelineRecord pipelineRecord) {
        String callbackUrl = pipelineRecord.getCallbackUrl();
        if (StringUtils.isBlank(callbackUrl)) {
            return;
        }

        log.info("start notice pipeline:{} start, address:{}", pipelineRecord.getPipelineId(), callbackUrl);
        PluginCallbackPayload callbackData = generateCallbackData(pipelineRecord);
        new HttpClient(callbackUrl).jsonBody(callbackData).post(BaseResponse.class);
        log.info("finished notice pipeline:{} start, address:{} ", pipelineRecord.getPipelineId(), callbackUrl);
    }

    @Override
    public void triggerHook(PipelineRecord pipelineRecord) {
        String callbackUrl = pipelineRecord.getCallbackUrl();
        if (StringUtils.isBlank(callbackUrl)) {
            return;
        }

        PluginCallbackPayload callbackData = generateCallbackData(pipelineRecord);
        log.info("start trigger pipeline:{} hook, address:{}, data:{}", pipelineRecord.getPipelineId(), callbackUrl, callbackData);
        new HttpClient(callbackUrl).jsonBody(callbackData).post(BaseResponse.class);
        log.info("finished notice pipeline:{} hook, address:{}, data:{} ", pipelineRecord.getPipelineId(), callbackUrl, callbackData);
    }

    private PluginCallbackPayload generateCallbackData(PipelineRecord pipelineRecord) {
        BuildStatus buildStatus = pipelineRecord.getStatus();
        PluginCallbackPayload callbackData = new PluginCallbackPayload();
        if (buildStatus == BuildStatus.SUCCESS) {
            callbackData.setJobEvent(JobEvent.SUCCESS);
        } else if (BuildStatus.FAIL == buildStatus || BuildStatus.CANCEL == buildStatus || BuildStatus.ABORT == buildStatus) {
            callbackData.setJobEvent(JobEvent.FAIL);
        } else {
            callbackData.setJobEvent(JobEvent.START);
        }

        callbackData.setData(pipelineRecord);
        return callbackData;
    }
}
