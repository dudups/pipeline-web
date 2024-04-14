package com.ezone.devops.pipeline.dao.impl;

import com.ezone.devops.pipeline.dao.WebHookNoticeEventDao;
import com.ezone.devops.pipeline.model.WebHookNoticeEvent;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import com.ezone.galaxy.fasterdao.param.NotParam;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public class WebHookNoticeEventDaoImpl extends BaseCommonDao<WebHookNoticeEvent> implements WebHookNoticeEventDao {


    @Override
    public boolean deleteByHookId(Long companyId, String repoKey, Long hookId) {
        return delete(match(COMPANY_ID, companyId), match(REPO_KEY, repoKey), match(HOOK_ID, hookId)) > 0;
    }

    @Override
    public boolean deleteByPipelineId(Long companyId, String repoKey, Long pipelineId) {
        return delete(match(COMPANY_ID, companyId), match(REPO_KEY, repoKey), match(PIPELINE_ID, pipelineId)) > 0;
    }

    @Override
    public List<WebHookNoticeEvent> getStart(Long companyId, String repoKey, Long pipelineId) {
        return find(match(COMPANY_ID, companyId), match(REPO_KEY, repoKey), match(PIPELINE_ID, pipelineId), match(START, true));
    }

    @Override
    public List<WebHookNoticeEvent> getSuccess(Long companyId, String repoKey, Long pipelineId) {
        return find(match(COMPANY_ID, companyId), match(REPO_KEY, repoKey), match(PIPELINE_ID, pipelineId), match(SUCCESS, true));
    }

    @Override
    public List<WebHookNoticeEvent> getFailed(Long companyId, String repoKey, Long pipelineId) {
        return find(match(COMPANY_ID, companyId), match(REPO_KEY, repoKey), match(PIPELINE_ID, pipelineId), match(FAILED, true));
    }

    @Override
    public List<WebHookNoticeEvent> getByHookId(Long companyId, Long hookId) {
        return find(match(COMPANY_ID, companyId), match(HOOK_ID, hookId));
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
