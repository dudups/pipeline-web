package com.ezone.devops.pipeline.dao.impl;

import com.ezone.devops.pipeline.dao.WebHookNoticeDao;
import com.ezone.devops.pipeline.model.WebHookNotice;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import com.ezone.galaxy.fasterdao.param.NotParam;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public class WebHookNoticeDaoImpl extends BaseCommonDao<WebHookNotice> implements WebHookNoticeDao {

    @Override
    public List<WebHookNotice> getByHookId(Long companyId, Long hookId) {
        return find(match(COMPANY_ID, companyId), match(HOOK_ID, hookId));
    }

    @Override
    public List<WebHookNotice> getByRepoKey(String repoKey) {
        return find(match(REPO_KEY, repoKey));
    }

    @Override
    public boolean deleteByHookId(Long companyId, String repoKey, Long hookId) {
        return delete(match(COMPANY_ID, companyId), match(REPO_KEY, repoKey), match(HOOK_ID, hookId)) > 0;
    }

    @Override
    public int deleteNotExistByRepoKeys(Set<String> repoKeys) {
        return delete(match(REPO_KEY, new NotParam(repoKeys)));
    }

    @Override
    public void deleteAll() {
        delete();
    }
}
