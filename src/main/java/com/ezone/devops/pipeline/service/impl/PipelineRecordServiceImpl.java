package com.ezone.devops.pipeline.service.impl;

import com.ezone.devops.ezcode.sdk.bean.enums.CardRelateType;
import com.ezone.devops.ezcode.sdk.service.InternalCardService;
import com.ezone.devops.ezcode.sdk.service.InternalCommitService;
import com.ezone.devops.ezcode.sdk.service.InternalReviewService;
import com.ezone.devops.pipeline.common.ScmTriggerType;
import com.ezone.devops.pipeline.common.TriggerMode;
import com.ezone.devops.pipeline.dao.PipelineRecordDao;
import com.ezone.devops.pipeline.enums.PipelinePermissionType;
import com.ezone.devops.pipeline.exception.PipelineRecordNotExistException;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.service.*;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.plugins.job.enums.VersionType;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.bean.HttpCode;
import com.ezone.galaxy.framework.common.bean.PageResult;
import com.ezone.galaxy.framework.redis.annotation.KLock;
import com.ezone.galaxy.framework.redis.enums.LockTimeoutStrategy;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PipelineRecordServiceImpl implements PipelineRecordService {

    @Autowired
    private VersionService versionService;
    @Autowired
    private PipelineRecordDao pipelineRecordDao;
    @Autowired
    private StageRecordService stageRecordService;
    @Autowired
    private BuildNumberService buildNumberService;
    @Autowired
    private PipelineService pipelineService;
    @Autowired
    private PipelinePermissionService pipelinePermissionService;
    @Autowired
    private RuntimeVariableService runtimeVariableService;
    @Autowired
    private InternalReviewService reviewService;
    @Autowired
    private InternalCommitService commitService;
    @Autowired
    private InternalCardService cardService;

    @Override
    public PipelineRecord getByIdIfPresent(Long pipelineId, Long recordId) {
        PipelineRecord pipelineRecord = pipelineRecordDao.getByPipelineAndId(pipelineId, recordId);
        if (pipelineRecord == null) {
            throw new PipelineRecordNotExistException();
        }
        return pipelineRecord;
    }

    @Override
    public PipelineRecord getByIdIfPresent(Long id) {
        PipelineRecord pipelineBuild = pipelineRecordDao.get(id);
        if (pipelineBuild == null) {
            throw new PipelineRecordNotExistException();
        }
        return pipelineBuild;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updatePipelineRecord(PipelineRecord pipelineRecord) {
        return pipelineRecordDao.update(pipelineRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deletePipelineRecord(PipelineRecord pipelineRecord) {
        stageRecordService.deleteByPipelineRecord(pipelineRecord);
        runtimeVariableService.deleteRuntimeVariables(pipelineRecord);
        pipelineRecordDao.delete(pipelineRecord);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteByRepo(RepoVo repo) {
        return pipelineRecordDao.deleteByRepoKey(repo.getRepoKey()) > 0;
    }

    @KLock(name = "init_pipeline", keys = {"#pipeline.id"}, leaseTime = 30, lockTimeoutStrategy = LockTimeoutStrategy.KEEP_ACQUIRE)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public PipelineRecord initPipelineRecord(Pipeline pipeline, RepoVo repo, ScmTriggerType scmTriggerType, String externalName,
                                             String commit, String user, TriggerMode mode, String externalKey,
                                             String dashboardUrl, String callbackUrl) {
        log.info("start init pipeline build, repo:[{}], externalName:[{}, {}]", repo.getRepoName(), externalName, commit);
        Long buildNumber = buildNumberService.getNextBuildNumber(pipeline);
        String snapshotVersion = versionService.getNextSnapshotVersion(repo);

        log.info("repo {} pipeline {} triggerMode {} generate snapshot version {}",
                repo.getRepoName(), pipeline.getName(), mode, snapshotVersion);
        PipelineRecord pipelineRecord = new PipelineRecord(pipeline, repo, scmTriggerType, externalName, commit,
                user, mode, buildNumber, snapshotVersion, externalKey, dashboardUrl, callbackUrl);
        pipelineRecord = pipelineRecordDao.save(pipelineRecord);
        stageRecordService.initStageRecords(repo, pipeline, pipelineRecord);
        log.info("finished init pipeline build, repo:[{}], branch:[{}, {}]", repo.getRepoName(), externalName, commit);
        return pipelineRecord;
    }

    @Override
    public PipelineRecord getLatestPipelineRecord(Pipeline pipeline, TriggerMode triggerMode) {
        return pipelineRecordDao.getLatestPipelineRecordByPipelineIdAndTriggerMode(pipeline.getId(), triggerMode);
    }

    @Override
    public List<PipelineRecord> getPipelineRecords(Long companyId, String repoKey, Long pipelineId, ScmTriggerType scmTriggerType,
                                                   String externalName, String externalKey) {
        return pipelineRecordDao.getPipelineRecords(repoKey, pipelineId, scmTriggerType, externalName, externalKey);
    }

    @Override
    public PageResult<List<PipelineRecord>> getPipelineRecords(RepoVo repo, String username, Long pipelineId,
                                                               ScmTriggerType scmTriggerType, String externalName,
                                                               String externalKey, String commit, VersionType versionType,
                                                               String version, int pageNumber, int pageSize) {
        List<Long> pipelineIds = getAuthorizedPipelineIds(repo, username, pipelineId);
        if (CollectionUtils.isEmpty(pipelineIds)) {
            return null;
        }

        return pipelineRecordDao.getBranchPipelineRecords(repo.getRepoKey(), pipelineIds, scmTriggerType, externalName, externalKey, commit, versionType,
                version, pageNumber, pageSize);
    }

    private List<Long> getAuthorizedPipelineIds(RepoVo repo, String username, Long pipelineId) {
        if (pipelineId == null) {
            List<Pipeline> pipelines = pipelineService.getAuthorizedPipeline(repo, username);
            if (CollectionUtils.isNotEmpty(pipelines)) {
                return pipelines.stream().map(Pipeline::getId).collect(Collectors.toList());
            }

            return null;
        } else {
            Pipeline pipeline = pipelineService.getByIdIfPresent(pipelineId);
            boolean hasPermission = pipelinePermissionService.hasPermission(repo, pipeline, PipelinePermissionType.PIPELINE_READER, username);
            if (!hasPermission) {
                return null;
            }

            return Lists.newArrayList(pipelineId);
        }
    }


    @Override
    public BaseResponse<?> getPipelineBuildRelateCards(RepoVo repo, PipelineRecord pipelineRecord, String[] fields) {
        CardRelateType relateType = TriggerMode.toCardRelateType(pipelineRecord.getTriggerMode());
        if (relateType == null) {
            return new BaseResponse<>(HttpCode.SUCCESS, null);
        }
        String relateKey = getCardRelateKey(pipelineRecord, relateType);
        if (StringUtils.isBlank(relateKey)) {
            return new BaseResponse<>(HttpCode.SUCCESS, null);
        }
        long companyId = repo.getCompanyId();
        long codeRepoId = Long.parseLong(repo.getRepoKey());
        return cardService.listPipelineBoundCards(companyId, codeRepoId, relateKey, relateType, fields);
    }

    private String getCardRelateKey(PipelineRecord pipelineRecord, CardRelateType relateType) {
        return CardRelateType.COMMIT == relateType ? pipelineRecord.getCommitId() : pipelineRecord.getExternalKey();
    }

    @Override
    public Object getTriggerModeDetail(PipelineRecord pipelineRecord) {
        String externalKey;
        if (pipelineRecord == null || StringUtils.isBlank(externalKey = pipelineRecord.getExternalKey())) {
            return null;
        }

        Pipeline pipeline = pipelineService.getByIdIfPresent(pipelineRecord.getPipelineId());
        Long companyId = pipeline.getCompanyId();
        String repoKey = pipelineRecord.getRepoKey();
        long codeRepoId = Long.parseLong(repoKey);
        if (TriggerMode.PUSH == pipelineRecord.getTriggerMode()) {
            return commitService.getPushCommits(companyId, codeRepoId, Long.parseLong(externalKey));
        }
        if (TriggerMode.isCodeReviewMode(pipelineRecord.getTriggerMode())) {
            return reviewService.getReview(companyId, codeRepoId, Long.parseLong(externalKey));
        }
        return null;
    }

    @Override
    public List<PipelineRecord> queryByCondition(Long companyId, Date date, int batchSize) {
        return pipelineRecordDao.queryByCondition(companyId, date, batchSize);
    }

}
