package com.ezone.devops.pipeline.dao.impl;

import com.ezone.devops.pipeline.common.ScmTriggerType;
import com.ezone.devops.pipeline.common.TriggerMode;
import com.ezone.devops.pipeline.dao.PipelineRecordDao;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.plugins.job.enums.VersionType;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import com.ezone.galaxy.fasterdao.operator.Match;
import com.ezone.galaxy.fasterdao.operator.Order;
import com.ezone.galaxy.fasterdao.page.Page;
import com.ezone.galaxy.fasterdao.param.NotParam;
import com.ezone.galaxy.framework.common.bean.PageResult;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Repository
public class PipelineRecordDaoImpl extends BaseCommonDao<PipelineRecord> implements PipelineRecordDao {

    private final String QUERY_SQL = "select " + ID + "," + COMPANY_ID + "," + PIPELINE_ID + ","
            + PIPELINE_NAME + "," + JOB_TIMEOUT_MINUTE + "," + SNAPSHOT_VERSION + "," + RELEASE_VERSION + ","
            + BUILD_NUMBER + "," + REPO_KEY + "," + SCM_TRIGGER_TYPE + "," + EXTERNAL_NAME + "," + COMMIT_ID + ","
            + STATUS + "," + TRIGGER_MODE + "," + TRIGGER_USER + "," + EXTERNAL_KEY + "," + DASHBOARD_URL + ","
            + CALLBACK_URL + "," + CREATE_TIME + "," + MODIFY_TIME + " from pipeline_record ";


    private final String COUNT_SQL = "select count(*) from pipeline_record ";

    @Override
    public PipelineRecord getByPipelineAndId(Long pipelineId, Long id) {
        return findOne(match(PIPELINE_ID, pipelineId), match(ID, id));
    }

    @Override
    public int deleteByCompanyId(Long companyId) {
        return delete(match(COMPANY_ID, companyId));
    }

    @Override
    public Long findMaxBuildNumberByPipelineId(Long pipelineId) {
        List<PipelineRecord> pipelineBuilds = page(match(PIPELINE_ID, pipelineId), order(BUILD_NUMBER, false), 0, 1);
        if (CollectionUtils.isEmpty(pipelineBuilds)) {
            return 0L;
        }

        return pipelineBuilds.get(0).getBuildNumber();
    }

    @Override
    public List<PipelineRecord> getPipelineRecords(String repoKey, Long pipelineId, ScmTriggerType scmTriggerType, String externalName, String externalKey) {
        List<Match> matches = Lists.newArrayList();
        matches.add(match(REPO_KEY, repoKey));
        if (pipelineId != null) {
            matches.add(match(PIPELINE_ID, pipelineId));
        }
        if (scmTriggerType != null) {
            matches.add(match(SCM_TRIGGER_TYPE, scmTriggerType));
        }
        if (StringUtils.isNotBlank(externalName)) {
            matches.add(match(EXTERNAL_NAME, externalName));
        }
        if (StringUtils.isNotBlank(externalKey)) {
            matches.add(match(EXTERNAL_KEY, externalKey));
        }

        return find(matches);
    }

    @Override
    public PageResult<List<PipelineRecord>> getBranchPipelineRecords(String repoKey, List<Long> pipelineIds,
                                                                     ScmTriggerType scmTriggerType, String externalName,
                                                                     String externalKey, String commit,
                                                                     VersionType versionType, String version,
                                                                     int pageNumber, int pageSize) {
        StringBuilder conditions = new StringBuilder();
        conditions.append(" where ");
        conditions.append(REPO_KEY).append(" = '").append(repoKey).append("' ");

        if (CollectionUtils.isNotEmpty(pipelineIds)) {
            conditions.append(" and ").append(PIPELINE_ID).append(" in ( ");
            for (int i = 0; i < pipelineIds.size(); i++) {
                Long pipeline = pipelineIds.get(i);
                if (i == pipelineIds.size() - 1) {
                    conditions.append(pipeline);
                } else {
                    conditions.append(pipeline).append(",");
                }
            }
            conditions.append(")");
        }

        if (scmTriggerType != null) {
            conditions.append(" and ").append(SCM_TRIGGER_TYPE).append(" = '").append(scmTriggerType.name()).append("' ");
        }
        if (StringUtils.isNotBlank(externalName)) {
            conditions.append(" and ").append(EXTERNAL_NAME).append(" = '").append(externalName).append("' ");
        }

        if (versionType == null) {
            if (StringUtils.isNotBlank(version)) {
                conditions.append(" and (")
                        .append(RELEASE_VERSION).append(" like '%").append(version).append("%' or ")
                        .append(SNAPSHOT_VERSION).append(" like '%").append(version).append("%'")
                        .append(") ");
            }
        } else {
            if (StringUtils.isNotBlank(version)) {
                if (versionType == VersionType.RELEASE) {
                    conditions.append(" and ").append(RELEASE_VERSION).append(" like '%").append(version).append("%' ");
                } else {
                    conditions.append(" and ").append(SNAPSHOT_VERSION).append(" like '%").append(version).append("%' ");
                }
            }
        }

        if (StringUtils.isNotBlank(commit)) {
            conditions.append(" and ").append(COMMIT_ID).append(" = '").append(commit).append("' ");
            ;
        }
        if (StringUtils.isNotBlank(externalKey)) {
            conditions.append(" and ").append(EXTERNAL_KEY).append(" = '").append(externalKey).append("' ");
        }

        String countSql = COUNT_SQL + conditions;
        int total = countBySQL(countSql, null);
        Page page = new Page(pageNumber, pageSize, total);

        conditions.append(" order by ").append(CREATE_TIME).append(" desc ");
        conditions.append(" limit ").append(page.getOffset()).append(", ").append(page.getPageSize());

        String querySql = QUERY_SQL + conditions;
        List<PipelineRecord> records = findBySQL(querySql, null);
        return new PageResult<>(total, records);
    }

    @Override
    public PipelineRecord getLatestPipelineRecordByPipelineIdAndTriggerMode(Long pipelineId, TriggerMode triggerMode) {
        List<Match> matches = Lists.newArrayList();
        matches.add(match(PIPELINE_ID, pipelineId));
        matches.add(match(TRIGGER_MODE, triggerMode));
        List<Order> orders = Lists.newArrayList(order(BUILD_NUMBER, false));
        List<PipelineRecord> pipelineBuilds = find(matches, orders, 0, 1);
        return CollectionUtils.isNotEmpty(pipelineBuilds) ? pipelineBuilds.get(0) : null;
    }

    @Override
    public List<PipelineRecord> queryByCondition(Long companyId, Date date, int batchSize) {
        List<Match> matches = Lists.newArrayList();
        matches.add(match(COMPANY_ID, companyId));
        matches.add(match(CREATE_TIME, lessThan(date)));
        return find(matches, null, 0, batchSize);
    }

    @Override
    public int deleteByRepoKey(String repoKey) {
        return delete(match(REPO_KEY, repoKey));
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
