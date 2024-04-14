package com.ezone.devops.pipeline.dao.impl;

import com.ezone.devops.pipeline.dao.PipelineDao;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import com.ezone.galaxy.fasterdao.param.NotParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public class PipelineDaoImpl extends BaseCommonDao<Pipeline> implements PipelineDao {

    @Override
    public Pipeline getByName(String repoKey, String pipelineName) {
        return findOne(match(REPO_KEY, repoKey), match(NAME, pipelineName));
    }

    @Override
    public List<Pipeline> getByRepoKey(Set<String> repoKeys) {
        return find(match(REPO_KEY, repoKeys));
    }

    @Override
    public List<Pipeline> getByRepoKey(String repoKey) {
        return find(match(REPO_KEY, repoKey));
    }

    @Override
    public List<Pipeline> searchByPipelineName(String repoKey, String pipelineName) {
        if (StringUtils.isBlank(pipelineName)) {
            return find(match(REPO_KEY, repoKey));
        }
        return find(match(REPO_KEY, repoKey), match(NAME, like(pipelineName, true)));
    }

    @Override
    public int countByCompanyId(Long companyId) {
        return count(match(COMPANY_ID, companyId));
    }

    @Override
    public int deleteNotExistByRepoKeys(Collection<String> repoKeys) {
        return delete(match(REPO_KEY, new NotParam(repoKeys)));
    }

    @Override
    public void deleteAll() {
        delete();
    }
}
