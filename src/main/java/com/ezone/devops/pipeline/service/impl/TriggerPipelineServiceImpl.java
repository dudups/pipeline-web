package com.ezone.devops.pipeline.service.impl;

import com.ezone.devops.ezcode.sdk.bean.model.InternalBranch;
import com.ezone.devops.ezcode.sdk.bean.model.InternalTag;
import com.ezone.devops.ezcode.sdk.service.InternalBranchService;
import com.ezone.devops.ezcode.sdk.service.InternalTagService;
import com.ezone.devops.pipeline.common.ScmTriggerType;
import com.ezone.devops.pipeline.common.TriggerMode;
import com.ezone.devops.pipeline.event.service.PipelineRecordEventService;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.mq.bean.TriggerMessage;
import com.ezone.devops.pipeline.service.*;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.pipeline.web.request.TriggerConfigPayload;
import com.ezone.devops.pipeline.web.request.TriggerPipelinePayload;
import com.ezone.devops.pipeline.web.request.VariablePair;
import com.ezone.ezbase.iam.bean.CompanyOverview;
import com.ezone.ezbase.iam.bean.enums.CompanyStatus;
import com.ezone.ezbase.iam.service.IAMCenterService;
import com.ezone.galaxy.framework.common.bean.BaseException;
import com.ezone.galaxy.framework.main.concurrent.aop.Pooled;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class TriggerPipelineServiceImpl implements TriggerPipelineService {

    private static final int MAX_MESSAGE_LENGTH = 255;

    @Autowired
    private RepoService repoService;
    @Autowired
    private IAMCenterService iamCenterService;
    @Autowired
    private InternalBranchService branchService;
    @Autowired
    private InternalTagService tagService;
    @Autowired
    private PipelineRecordService pipelineRecordService;
    @Autowired
    private PipelineTriggerService pipelineTriggerService;
    @Autowired
    private PipelineRecordEventService pipelineRecordEventService;
    @Autowired
    private BranchPatternConfigService branchPatternConfigService;
    @Autowired
    private RuntimeVariableService runtimeVariableService;

    @Pooled(timeout = 20000)
    @Override
    public boolean asyncCrontabTriggerPipeline(Pipeline pipeline) {
        RepoVo repo = repoService.getByRepoKeyIfPresent(pipeline.getCompanyId(), pipeline.getRepoKey());
        CompanyOverview companyOverview = iamCenterService.queryCompanyOverviewByCompanyId(repo.getCompanyId());
        CompanyStatus status = companyOverview.getStatus();
        if (status == CompanyStatus.FREEZE || status == CompanyStatus.SHUTDOWN) {
            return false;
        }

        List<InternalBranch> branches = branchPatternConfigService.getMatchedBranches(repo, pipeline);
        if (CollectionUtils.isEmpty(branches)) {
            return false;
        }

        TriggerConfigPayload triggerConfigPayload = pipelineTriggerService.getTriggerConfigPayload(pipeline);
        if (triggerConfigPayload == null || triggerConfigPayload.getTriggerMode() != TriggerMode.CRONTAB) {
            return false;
        }

        PipelineRecord latestRecord = pipelineRecordService.getLatestPipelineRecord(pipeline, TriggerMode.CRONTAB);
        for (InternalBranch branch : branches) {
            if (!triggerConfigPayload.isPollScm()) {
                doBuild(pipeline, repo, ScmTriggerType.BRANCH, branch.getName(), branch.getCommitId(), null, pipeline.getCreateUser(), TriggerMode.CRONTAB, StringUtils.EMPTY);
            } else {
                // 如果上次定时任务触发的 commit 和分支最新的 head 一样，则不触发
                if (latestRecord != null && latestRecord.getCommitId().equals(branch.getCommitId())) {
                    log.info("branch:[{}] commit:[{}] not change", branch.getName(), branch.getCommitId());
                } else {
                    doBuild(pipeline, repo, ScmTriggerType.BRANCH, branch.getName(), branch.getCommitId(), null, pipeline.getCreateUser(), TriggerMode.CRONTAB, StringUtils.EMPTY);
                }
            }
        }
        return true;
    }

    @Override
    public PipelineRecord triggerPipeline(Pipeline pipeline, TriggerPipelinePayload payload, String user, TriggerMode mode, String callbackUrl) {
        Set<VariablePair> variables = payload.getVariables();
        RepoVo repoVo = repoService.getByRepoKeyIfPresent(pipeline.getCompanyId(), pipeline.getRepoKey());
        String externalName = payload.getExternalName();
        String commitId = payload.getExternalName();
        ScmTriggerType scmTriggerType = payload.getScmTriggerType();
        if (scmTriggerType == ScmTriggerType.BRANCH) {
            InternalBranch branch = branchService.getBranch(pipeline.getCompanyId(), repoVo.getRepoKey(), externalName);
            if (branch == null) {
                throw new BaseException(HttpStatus.NOT_FOUND.value(), "分支不存在");
            }
            commitId = branch.getCommitId();
        } else if (scmTriggerType == ScmTriggerType.TAG) {
            InternalTag internalTag = tagService.getTagByName(pipeline.getCompanyId(), repoVo.getLongRepoKey(), externalName);
            if (internalTag == null) {
                throw new BaseException(HttpStatus.NOT_FOUND.value(), "标签不存在");
            }
            commitId = internalTag.getCommitId();
        }

        return doBuild(pipeline, repoVo, scmTriggerType, externalName, commitId, variables, user, mode, callbackUrl);
    }

    @Override
    public PipelineRecord triggerPipelineOnlyForBranch(Pipeline pipeline, RepoVo repo, TriggerMessage message) {
        ScmTriggerType scmTriggerType = ScmTriggerType.BRANCH;
        InternalBranch branch = InternalBranch.toTempBranch(message.getBranchName(), message.getCommitId());
        String callbackUrl = message.getCallbackUrl();
        return doBuild(pipeline, repo, scmTriggerType, branch.getName(), branch.getCommitId(), null, message.getOperator(),
                message.getTriggerMode(), message.getExternalKey(), message.getDashboardUrl(), StringUtils.defaultString(callbackUrl, StringUtils.EMPTY));
    }

    private PipelineRecord doBuild(Pipeline pipeline, RepoVo repo, ScmTriggerType scmTriggerType, String externalName,
                                   String commit, Set<VariablePair> variables, String user, TriggerMode mode, String callbackUrl) {
        return doBuild(pipeline, repo, scmTriggerType, externalName, commit, variables, user, mode, StringUtils.EMPTY, StringUtils.EMPTY, callbackUrl);
    }

    private PipelineRecord doBuild(Pipeline pipeline, RepoVo repo, ScmTriggerType scmTriggerType, String externalName,
                                   String commit, Set<VariablePair> variables, String user, TriggerMode mode,
                                   String externalKey, String dashboardUrl, String callbackUrl) {
        PipelineRecord record = pipelineRecordService.initPipelineRecord(
                pipeline, repo, scmTriggerType, externalName, commit, user, mode,
                getValidLengthMessage(externalKey),
                getValidLengthMessage(dashboardUrl),
                StringUtils.defaultString(callbackUrl, StringUtils.EMPTY)
        );

        runtimeVariableService.initRuntimeVariables(pipeline, record, variables);
        pipelineRecordEventService.startPipelineRecordEvent(record, user);
        return record;
    }

    private String getValidLengthMessage(String externalKey) {
        if (StringUtils.isBlank(externalKey)) {
            return StringUtils.EMPTY;
        }
        return externalKey.substring(0, Math.min(MAX_MESSAGE_LENGTH, StringUtils.length(externalKey)));
    }

}
