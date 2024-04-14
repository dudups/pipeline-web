package com.ezone.devops.pipeline.dao;

import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface PipelineDao extends LongKeyBaseDao<Pipeline> {

    String ID = "id";
    String COMPANY_ID = "company_id";
    String REPO_KEY = "repo_key";
    String NAME = "name";
    String JOB_TIMEOUT_MINUTE = "job_timeout_minute";
    String MATCH_ALL_BRANCH = "match_all_branch";
    String USE_DEFAULT_CLUSTER = "use_default_cluster";
    String RESOURCE_TYPE = "resource_type";
    String CLUSTER_NAME = "cluster_name";
    String CREATE_USER = "create_user";
    String CREATE_TIME = "create_time";
    String MODIFY_TIME = "modify_time";
    String INHERIT_REPO_PERMISSION = "inherit_repo_permission";

    /**
     * 按照代码库、流水线名称和删除状态查询
     */
    Pipeline getByName(String repoKey, String pipelineName);

    List<Pipeline> getByRepoKey(Set<String> repoKeys);

    List<Pipeline> getByRepoKey(String repoKey);

    List<Pipeline> searchByPipelineName(String repoKey, String pipelineName);

    int countByCompanyId(Long companyId);


    /**
     * 删除不存在的代码库数据
     *
     * @param repoKeys
     */
    int deleteNotExistByRepoKeys(Collection<String> repoKeys);

    void deleteAll();

}
