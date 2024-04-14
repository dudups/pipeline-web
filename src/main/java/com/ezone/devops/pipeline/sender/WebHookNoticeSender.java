package com.ezone.devops.pipeline.sender;

import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.model.WebHookNotice;
import com.ezone.devops.pipeline.model.WebHookNoticeEvent;
import com.ezone.devops.pipeline.mq.bean.ResultType;
import com.ezone.devops.pipeline.sender.webhook.WebHookNoticeProcess;
import com.ezone.devops.pipeline.sender.webhook.WebHookNoticeProcessFactory;
import com.ezone.devops.pipeline.service.WebHookNoticeService;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.ezbase.iam.bean.WebHookMsg;
import com.ezone.ezbase.iam.bean.enums.SystemType;
import com.ezone.ezbase.iam.dal.model.WebHook;
import com.ezone.ezbase.iam.service.IAMCenterService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class WebHookNoticeSender {

    @Autowired
    private IAMCenterService iamCenterService;
    @Autowired
    private WebHookNoticeService webHookNoticeService;
    @Autowired
    private WebHookNoticeProcessFactory webHookNoticeProcessFactory;

    public void invokeStartHook(RepoVo repo, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        List<WebHookNotice> webHookNotices = webHookNoticeService.getWebHooks(repo);
        if (CollectionUtils.isEmpty(webHookNotices)) {
            return;
        }

        for (WebHookNotice webHookNotice : webHookNotices) {
            if (webHookNotice.isAllPipeline() && webHookNotice.isStart()) {
                List<WebHook> webHooks = iamCenterService.queryHooksByIds(Lists.newArrayList(webHookNotice.getHookId()));
                if (CollectionUtils.isEmpty(webHooks)) {
                    return;
                }
                for (WebHook webHook : webHooks) {
                    sendWebHookMsg(webHook, repo, pipelineRecord, jobRecord, ResultType.START);
                }
            } else {
                List<WebHookNoticeEvent> events = webHookNoticeService.getByStartEvent(repo.getCompanyId(),
                        repo.getRepoKey(), pipelineRecord.getPipelineId());
                send(events, repo, pipelineRecord, jobRecord, ResultType.START);
            }
        }
    }

    public void invokeSuccessHook(RepoVo repo, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        List<WebHookNotice> webHookNotices = webHookNoticeService.getWebHooks(repo);
        if (CollectionUtils.isEmpty(webHookNotices)) {
            return;
        }

        for (WebHookNotice webHookNotice : webHookNotices) {
            if (webHookNotice.isAllPipeline() && webHookNotice.isSuccess()) {
                List<WebHook> webHooks = iamCenterService.queryHooksByIds(Lists.newArrayList(webHookNotice.getHookId()));
                if (CollectionUtils.isEmpty(webHooks)) {
                    return;
                }
                for (WebHook webHook : webHooks) {
                    sendWebHookMsg(webHook, repo, pipelineRecord, jobRecord, ResultType.SUCCESS);
                }
            } else {
                List<WebHookNoticeEvent> events = webHookNoticeService
                        .getBySuccessEvent(repo.getCompanyId(), repo.getRepoKey(), pipelineRecord.getPipelineId());
                send(events, repo, pipelineRecord, jobRecord, ResultType.SUCCESS);
            }
        }
    }

    public void invokeFailedHook(RepoVo repo, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        List<WebHookNotice> webHookNotices = webHookNoticeService.getWebHooks(repo);
        if (CollectionUtils.isEmpty(webHookNotices)) {
            return;
        }

        for (WebHookNotice webHookNotice : webHookNotices) {
            if (webHookNotice.isAllPipeline() && webHookNotice.isFailed()) {
                List<WebHook> webHooks = iamCenterService.queryHooksByIds(Lists.newArrayList(webHookNotice.getHookId()));
                if (CollectionUtils.isEmpty(webHooks)) {
                    return;
                }
                for (WebHook webHook : webHooks) {
                    sendWebHookMsg(webHook, repo, pipelineRecord, jobRecord, ResultType.FAIL);
                }
            } else {
                List<WebHookNoticeEvent> events = webHookNoticeService
                        .getByFailedEvent(repo.getCompanyId(), repo.getRepoKey(), pipelineRecord.getPipelineId());
                send(events, repo, pipelineRecord, jobRecord, ResultType.FAIL);
            }
        }
    }

    private void send(List<WebHookNoticeEvent> events, RepoVo repo, PipelineRecord pipelineRecord, JobRecord jobRecord, ResultType resultType) {
        if (CollectionUtils.isEmpty(events)) {
            return;
        }

        Map<Long, List<WebHookNoticeEvent>> relations = new HashMap<>();
        for (WebHookNoticeEvent event : events) {
            Long hookId = event.getHookId();
            if (relations.containsKey(hookId)) {
                relations.get(hookId).add(event);
            } else {
                relations.put(hookId, Lists.newArrayList(event));
            }
        }

        List<WebHook> webHooks = iamCenterService.queryHooksByIds(new ArrayList<>(relations.keySet()));
        if (CollectionUtils.isEmpty(webHooks)) {
            return;
        }

        for (WebHook webHook : webHooks) {
            Long hookId = webHook.getId();
            if (!relations.containsKey(hookId)) {
                continue;
            }

            sendWebHookMsg(webHook, repo, pipelineRecord, jobRecord, resultType);
        }
    }


    private void sendWebHookMsg(WebHook webHook, RepoVo repo, PipelineRecord pipelineRecord, JobRecord jobRecord, ResultType resultType) {
        WebHookNoticeProcess process = webHookNoticeProcessFactory.getProcess(webHook.getPlatform());
        String requestBody = process.getMessageBody(repo, pipelineRecord, jobRecord, resultType);
        WebHookMsg hookMsg = new WebHookMsg();
        hookMsg.setWebHookId(webHook.getId());
        hookMsg.setType(SystemType.EZPIPELINE);
        hookMsg.setEventName(resultType.name());
        hookMsg.setResourceName(jobRecord.getName());
        hookMsg.setRequestBody(requestBody);
        hookMsg.setTimestamp(System.currentTimeMillis());
        iamCenterService.sendWebHook(hookMsg);
    }
}
