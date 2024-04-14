package com.ezone.devops.pipeline.dao;

import com.ezone.devops.pipeline.common.ScmTriggerType;
import com.ezone.devops.pipeline.common.TriggerMode;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.plugins.job.enums.VersionType;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;
import com.ezone.galaxy.framework.common.bean.PageResult;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface PipelineRecordDao extends LongKeyBaseDao<PipelineRecord> {

    String ID = "id";
    String COMPANY_ID = "company_id";
    String PIPELINE_ID = "pipeline_id";
    String PIPELINE_NAME = "pipeline_name";
    String JOB_TIMEOUT_MINUTE = "job_timeout_minute";
    String SNAPSHOT_VERSION = "snapshot_version";
    String RELEASE_VERSION = "release_version";
    String BUILD_NUMBER = "build_number";
    String REPO_KEY = "repo_key";
    String SCM_TRIGGER_TYPE = "scm_trigger_type";
    String EXTERNAL_NAME = "external_name";
    String COMMIT_ID = "commit_id";
    String STATUS = "status";
    String TRIGGER_MODE = "trigger_mode";
    String TRIGGER_USER = "trigger_user";
    String EXTERNAL_KEY = "external_key";
    String DASHBOARD_URL = "dashboard_url";
    String CALLBACK_URL = "callback_url";
    String CREATE_TIME = "create_time";
    String MODIFY_TIME = "modify_time";

    PipelineRecord getByPipelineAndId(Long pipelineId, Long id);

    int deleteByCompanyId(Long companyId);

    Long findMaxBuildNumberByPipelineId(Long pipelineId);

    List<PipelineRecord> getPipelineRecords(String repoKey, Long pipelineId, ScmTriggerType scmTriggerType, String externalName, String externalKey);

    PageResult<List<PipelineRecord>> getBranchPipelineRecords(String repoKey, List<Long> pipelineIds,
                                                              ScmTriggerType scmTriggerType,
                                                              String externalName, String externalKey, String commit,
                                                              VersionType versionType, String version,
                                                              int pageNumber, int pageSize);

    PipelineRecord getLatestPipelineRecordByPipelineIdAndTriggerMode(Long pipelineConfigId, TriggerMode triggerMode);

    List<PipelineRecord> queryByCondition(Long companyId, Date date, int batchSize);

    int deleteByRepoKey(String repoKey);

    int deleteNotExistByRepoKeys(Collection<String> repoKeys);

    void deleteAll();


}
