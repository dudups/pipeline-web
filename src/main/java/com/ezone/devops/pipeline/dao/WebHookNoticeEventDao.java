package com.ezone.devops.pipeline.dao;

import com.ezone.devops.pipeline.model.WebHookNoticeEvent;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

import java.util.List;
import java.util.Set;

public interface WebHookNoticeEventDao extends LongKeyBaseDao<WebHookNoticeEvent> {

    String ID = "id";
    String COMPANY_ID = "company_id";
    String REPO_KEY = "repo_key";
    String HOOK_ID = "hook_id";
    String PIPELINE_ID = "pipeline_id";
    String START = "start";
    String SUCCESS = "success";
    String FAILED = "failed";

    boolean deleteByHookId(Long companyId, String repoKey, Long hookId);

    boolean deleteByPipelineId(Long companyId, String repoKey, Long pipelineId);

    List<WebHookNoticeEvent> getStart(Long companyId, String repoKey, Long pipelineId);

    List<WebHookNoticeEvent> getSuccess(Long companyId, String repoKey, Long pipelineId);

    List<WebHookNoticeEvent> getFailed(Long companyId, String repoKey, Long pipelineId);

    List<WebHookNoticeEvent> getByHookId(Long companyId, Long hookId);

    int deleteNotExistByRepoKeys(Set<String> repoKeys);

    void deleteAll();

}
