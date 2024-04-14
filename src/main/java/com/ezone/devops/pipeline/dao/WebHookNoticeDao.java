package com.ezone.devops.pipeline.dao;

import com.ezone.devops.pipeline.model.WebHookNotice;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

import java.util.List;
import java.util.Set;

public interface WebHookNoticeDao extends LongKeyBaseDao<WebHookNotice> {

    String ID = "id";
    String COMPANY_ID = "company_id";
    String REPO_KEY = "repo_key";
    String ALL_PIPELINE = "all_pipeline";
    String START = "start";
    String SUCCESS = "success";
    String FAILED = "failed";
    String HOOK_ID = "hook_id";

    List<WebHookNotice> getByHookId(Long companyId, Long hookId);

    List<WebHookNotice> getByRepoKey(String repoKey);

    boolean deleteByHookId(Long companyId, String repoKey, Long hookId);

    int deleteNotExistByRepoKeys(Set<String> repoKeys);

    void deleteAll();

}
