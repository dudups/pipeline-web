package com.ezone.devops.pipeline.model;

import com.ezone.devops.pipeline.dao.WebHookNoticeEventDao;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.pipeline.web.request.WebHookNoticePayload;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "web_hook_notice_event")
public class WebHookNoticeEvent extends LongID {

    @Column(WebHookNoticeEventDao.ID)
    private Long id;
    @Column(WebHookNoticeEventDao.COMPANY_ID)
    private Long companyId;
    @Column(WebHookNoticeEventDao.REPO_KEY)
    private String repoKey;
    @Column(WebHookNoticeEventDao.HOOK_ID)
    private Long hookId;
    @Column(WebHookNoticeEventDao.PIPELINE_ID)
    private Long pipelineId;
    @Column(WebHookNoticeEventDao.START)
    private boolean start;
    @Column(WebHookNoticeEventDao.SUCCESS)
    private boolean success;
    @Column(WebHookNoticeEventDao.FAILED)
    private boolean failed;

    public WebHookNoticeEvent(RepoVo repo, Long hookId, WebHookNoticePayload.WebHookNoticeEventPayload payload) {
        setRepoKey(repo.getRepoKey());
        setCompanyId(repo.getCompanyId());
        setHookId(hookId);
        setPipelineId(payload.getPipelineId());
        setStart(payload.isStart());
        setSuccess(payload.isSuccess());
        setFailed(payload.isFailed());
    }
}
