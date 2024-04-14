package com.ezone.devops.pipeline.service;

import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.WebHookNotice;
import com.ezone.devops.pipeline.model.WebHookNoticeEvent;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.pipeline.web.request.WebHookNoticePayload;

import java.util.List;

public interface WebHookNoticeService {

    boolean createOrUpdateWebHookNotice(Long hookId, String repoName, WebHookNoticePayload payload);

    boolean deleteWebHookNotice(RepoVo repo, Pipeline pipeline);

    boolean deleteWebHookNotice(RepoVo repo, Long hookId);

    List<WebHookNotice> getWebHooks(RepoVo repo);

    List<WebHookNoticePayload> getWebHookNotice(Long companyId, Long hookId);

    List<WebHookNoticeEvent> getByStartEvent(Long companyId, String repoKey, Long pipelineId);

    List<WebHookNoticeEvent> getBySuccessEvent(Long companyId, String repoKey, Long pipelineId);

    List<WebHookNoticeEvent> getByFailedEvent(Long companyId, String repoKey, Long pipelineId);
}
