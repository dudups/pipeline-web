package com.ezone.devops.pipeline.service.impl;

import com.ezone.devops.pipeline.context.RepoContext;
import com.ezone.devops.pipeline.dao.WebHookNoticeDao;
import com.ezone.devops.pipeline.dao.WebHookNoticeEventDao;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.WebHookNotice;
import com.ezone.devops.pipeline.model.WebHookNoticeEvent;
import com.ezone.devops.pipeline.service.RepoService;
import com.ezone.devops.pipeline.service.WebHookNoticeService;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.pipeline.web.request.WebHookNoticePayload;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class WebHookNoticeServiceImpl implements WebHookNoticeService {

    @Autowired
    private RepoService repoService;
    @Autowired
    private WebHookNoticeDao webHookNoticeDao;
    @Autowired
    private WebHookNoticeEventDao webHookNoticeEventDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean createOrUpdateWebHookNotice(Long hookId, String repoName, WebHookNoticePayload payload) {
        RepoVo repo = RepoContext.get();
        deleteWebHookNotice(hookId, repo);

        WebHookNotice webHookNotice = new WebHookNotice(repo, payload, hookId);
        webHookNoticeDao.save(webHookNotice);

        List<WebHookNoticeEvent> webHookNoticeEvents = new ArrayList<>();
        for (WebHookNoticePayload.WebHookNoticeEventPayload noticeEvent : payload.getNoticeEvents()) {
            webHookNoticeEvents.add(new WebHookNoticeEvent(repo, hookId, noticeEvent));
        }

        if (CollectionUtils.isNotEmpty(webHookNoticeEvents)) {
            webHookNoticeEventDao.save(webHookNoticeEvents);
        }

        return true;
    }

    @Override
    public boolean deleteWebHookNotice(RepoVo repo, Pipeline pipeline) {
        webHookNoticeEventDao.deleteByPipelineId(repo.getCompanyId(), repo.getRepoKey(), pipeline.getId());
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteWebHookNotice(RepoVo repo, Long hookId) {
        return deleteWebHookNotice(hookId, repo);
    }

    public boolean deleteWebHookNotice(Long hookId, RepoVo repo) {
        webHookNoticeDao.deleteByHookId(repo.getCompanyId(), repo.getRepoKey(), hookId);
        webHookNoticeEventDao.deleteByHookId(repo.getCompanyId(), repo.getRepoKey(), hookId);
        return true;
    }

    @Override
    public List<WebHookNotice> getWebHooks(RepoVo repo) {
        return webHookNoticeDao.getByRepoKey(repo.getRepoKey());
    }

    @Override
    public List<WebHookNoticePayload> getWebHookNotice(Long companyId, Long hookId) {
        List<WebHookNoticePayload> result = new ArrayList<>();
        List<WebHookNotice> webHookNotices = webHookNoticeDao.getByHookId(companyId, hookId);
        if (CollectionUtils.isEmpty(webHookNotices)) {
            return result;
        }

        Set<String> repoKeys = new HashSet<>(webHookNotices.size());
        Map<String, WebHookNotice> hookRelations = new HashMap<>(webHookNotices.size());
        for (WebHookNotice webHookNotice : webHookNotices) {
            hookRelations.put(webHookNotice.getRepoKey(), webHookNotice);
            repoKeys.add(webHookNotice.getRepoKey());
        }

        List<RepoVo> repos = repoService.getReposByKeys(companyId, repoKeys);
        if (CollectionUtils.isEmpty(repos)) {
            return result;
        }

        Map<String, Set<WebHookNoticePayload.WebHookNoticeEventPayload>> relations = new HashMap<>();
        List<WebHookNoticeEvent> events = webHookNoticeEventDao.getByHookId(companyId, hookId);
        if (CollectionUtils.isNotEmpty(events)) {
            for (WebHookNoticeEvent event : events) {
                String repoKey = event.getRepoKey();
                WebHookNoticePayload.WebHookNoticeEventPayload payload = assemble(event);
                if (relations.containsKey(repoKey)) {
                    relations.get(repoKey).add(payload);
                } else {
                    Set<WebHookNoticePayload.WebHookNoticeEventPayload> payloads = new HashSet<>();
                    payloads.add(payload);
                    relations.put(repoKey, payloads);
                }
            }
        }

        for (RepoVo repo : repos) {
            String repoKey = repo.getRepoKey();
            WebHookNotice webHookNotice = hookRelations.get(repoKey);
            WebHookNoticePayload payload = new WebHookNoticePayload();
            payload.setRepoName(repo.getRepoName());
            payload.setAllPipeline(webHookNotice.isAllPipeline());
            payload.setStart(webHookNotice.isStart());
            payload.setSuccess(webHookNotice.isSuccess());
            payload.setFailed(webHookNotice.isFailed());
            payload.setNoticeEvents(relations.get(repoKey));
            result.add(payload);
        }

        return result;
    }

    private WebHookNoticePayload.WebHookNoticeEventPayload assemble(WebHookNoticeEvent event) {
        WebHookNoticePayload.WebHookNoticeEventPayload payload = new WebHookNoticePayload.WebHookNoticeEventPayload();
        payload.setPipelineId(event.getPipelineId());
        payload.setStart(event.isStart());
        payload.setSuccess(event.isSuccess());
        payload.setFailed(event.isFailed());
        return payload;
    }

    @Override
    public List<WebHookNoticeEvent> getByStartEvent(Long companyId, String repoKey, Long pipelineId) {
        return webHookNoticeEventDao.getStart(companyId, repoKey, pipelineId);
    }

    @Override
    public List<WebHookNoticeEvent> getBySuccessEvent(Long companyId, String repoKey, Long pipelineId) {
        return webHookNoticeEventDao.getSuccess(companyId, repoKey, pipelineId);
    }

    @Override
    public List<WebHookNoticeEvent> getByFailedEvent(Long companyId, String repoKey, Long pipelineId) {
        return webHookNoticeEventDao.getFailed(companyId, repoKey, pipelineId);
    }
}
