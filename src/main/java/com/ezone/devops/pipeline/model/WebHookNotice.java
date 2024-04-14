package com.ezone.devops.pipeline.model;

import com.ezone.devops.pipeline.dao.WebHookNoticeDao;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.pipeline.web.request.WebHookNoticePayload;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "web_hook_notice")
public class WebHookNotice extends LongID {

    @Column(WebHookNoticeDao.ID)
    private Long id;
    @Column(WebHookNoticeDao.COMPANY_ID)
    private Long companyId;
    @Column(WebHookNoticeDao.REPO_KEY)
    private String repoKey;
    @Column(WebHookNoticeDao.ALL_PIPELINE)
    private boolean allPipeline;
    @Column(WebHookNoticeDao.START)
    private boolean start;
    @Column(WebHookNoticeDao.SUCCESS)
    private boolean success;
    @Column(WebHookNoticeDao.FAILED)
    private boolean failed;
    @Column(WebHookNoticeDao.HOOK_ID)
    private Long hookId;

    public WebHookNotice(RepoVo repo, WebHookNoticePayload payload, Long hookId) {
        setCompanyId(repo.getCompanyId());
        setRepoKey(repo.getRepoKey());
        setAllPipeline(payload.isAllPipeline());
        setStart(payload.isStart());
        setSuccess(payload.isSuccess());
        setFailed(payload.isFailed());
        setHookId(hookId);
    }
}
