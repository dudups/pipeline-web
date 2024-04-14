package com.ezone.devops.pipeline.service.impl;

import com.ezone.devops.pipeline.context.RepoContext;
import com.ezone.devops.pipeline.dao.PipelineDao;
import com.ezone.devops.pipeline.enums.ConditionTriggerType;
import com.ezone.devops.pipeline.enums.JobConditionType;
import com.ezone.devops.pipeline.exception.*;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.service.*;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.pipeline.web.request.*;
import com.ezone.devops.pipeline.web.response.PipelineVo;
import com.ezone.ezbase.iam.bean.CompanyIdentityUser;
import com.ezone.ezbase.iam.bean.enums.UserIdentityType;
import com.ezone.ezbase.iam.service.IAMCenterService;
import com.ezone.galaxy.fasterdao.context.ShardingContext;
import com.ezone.galaxy.framework.common.bean.BaseException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PipelineServiceImpl implements PipelineService {

    @Autowired
    private PipelineDao pipelineDao;
    @Autowired
    private StageService stageService;
    @Autowired
    private VariableService variableService;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private PipelineTriggerService pipelineTriggerService;
    @Autowired
    private IAMCenterService iamCenterService;
    @Autowired
    private RepoService repoService;
    @Autowired
    private PipelinePermissionService pipelinePermissionService;
    @Autowired
    private BranchPatternConfigService branchPatternConfigService;
    @Autowired
    private WebHookNoticeService webHookNoticeService;

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public Pipeline savePipeline(String repoName, PipelinePayload payload, String username) {
        RepoVo repo = RepoContext.get();
        checkCreatePipelineConfig(repo, payload);
        return savePipelineConfig(repo, payload, username);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Pipeline updatePipeline(RepoVo repo, Pipeline pipeline, PipelinePayload payload, String username) {
        checkUpdatePipelineConfig(repo, pipeline, payload);
        return updatePipelineConfig(repo, pipeline, payload, username);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Pipeline updatePipeline(Pipeline pipeline) {
        pipelineDao.update(pipeline);
        return pipeline;
    }

    @Override
    public Map<String, List<PipelineVo>> getByRepoKeys(Set<String> repoKeys) {
        if (CollectionUtils.isEmpty(repoKeys)) {
            return null;
        }
        List<Pipeline> pipelines = pipelineDao.getByRepoKey(repoKeys);
        if (CollectionUtils.isEmpty(pipelines)) {
            return null;
        }

        Map<String, List<PipelineVo>> result = Maps.newHashMapWithExpectedSize(pipelines.size());
        for (Pipeline pipeline : pipelines) {
            String repoKey = pipeline.getRepoKey();
            if (result.containsKey(repoKey)) {
                result.get(repoKey).add(new PipelineVo(pipeline));
            } else {
                result.put(repoKey, Lists.newArrayList(new PipelineVo(pipeline)));
            }
        }

        return result;
    }

    @Override
    public Pipeline getPipeline(RepoVo repo, String pipelineName) {
        return pipelineDao.getByName(repo.getRepoKey(), pipelineName);
    }

    @Override
    public PipelinePayload getPipelinePayload(Long id) {
        Pipeline pipeline = getByIdIfPresent(id);
        List<StagePayload> stagePayload = stageService.getStagePayloads(pipeline);
        PipelinePayload pipelinePayload = new PipelinePayload();
        BeanUtils.copyProperties(pipeline, pipelinePayload);

        RepoVo repo = repoService.getByRepoKeyIfPresent(pipeline.getCompanyId(), pipeline.getRepoKey());
        pipelinePayload.setRepoName(repo.getRepoName());

        List<BranchPatternConfigPayload> branchPatternBean = branchPatternConfigService.getBranchPatternPayload(pipeline);
        pipelinePayload.setBranchPatternConfig(branchPatternBean);

        TriggerConfigPayload triggerConfigPayload = pipelineTriggerService.getTriggerConfigPayload(pipeline);
        pipelinePayload.setPipelineTriggerConfig(triggerConfigPayload);

        List<EnvConfigPayload> envConfigPayloads = variableService.filterSecretVariablePayload(pipeline);
        pipelinePayload.setEnvConfig(envConfigPayloads);

        List<NoticeConfigPayload> noticeConfigPayload = noticeService.getNoticeConfigPayload(pipeline);
        pipelinePayload.setNoticeConfig(noticeConfigPayload);

        pipelinePayload.setStageConfig(stagePayload);
        return pipelinePayload;
    }

    @Override
    public int countByCompanyId(Long companyId) {
        return pipelineDao.countByCompanyId(companyId);
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public boolean deletePipeline(Pipeline pipeline) {
        ShardingContext.putShardKey(pipeline.getId());
        RepoVo repo = repoService.getByRepoKeyIfPresent(pipeline.getCompanyId(), pipeline.getRepoKey());
        pipelineTriggerService.deletePipelineCrontabConfig(pipeline, repo);
        stageService.deleteStage(pipeline);
        webHookNoticeService.deleteWebHookNotice(repo, pipeline);
        return pipelineDao.delete(pipeline);
    }

    @Override
    public List<Pipeline> getAuthorizedPipeline(RepoVo repo, String username) {
        return getAuthorizedPipeline(repo, username, StringUtils.EMPTY);
    }

    @Override
    public List<Pipeline> getAuthorizedPipeline(RepoVo repo, String username, String pipelineName) {
        CompanyIdentityUser companyIdentityUser = iamCenterService.queryUserByCompanyIdAndUsername(repo.getCompanyId(), username);
        List<Pipeline> pipelines = pipelineDao.searchByPipelineName(repo.getRepoKey(), pipelineName);
        if (companyIdentityUser.getType() == UserIdentityType.ADMIN) {
            return pipelines;
        }

        boolean repoAdmin = repoService.isRepoAdmin(repo, username);
        if (repoAdmin) {
            return pipelines;
        }

        if (CollectionUtils.isEmpty(pipelines)) {
            return null;
        }

        TreeSet<Long> authorizedPipelineIds = pipelinePermissionService.getAuthorizedPipeline(repo, username);
        if (CollectionUtils.isEmpty(pipelines) && CollectionUtils.isEmpty(authorizedPipelineIds)) {
            return null;
        }

        List<Pipeline> result = Lists.newArrayListWithCapacity(pipelines.size());
        for (Pipeline pipeline : pipelines) {
            if (pipeline.isInheritRepoPermission()) {
                result.add(pipeline);
            } else {
                if (CollectionUtils.isEmpty(authorizedPipelineIds)) {
                    continue;
                }
                if (authorizedPipelineIds.contains(pipeline.getId())) {
                    result.add(pipeline);
                }
            }
        }
        return result;
    }

    private Pipeline savePipelineConfig(RepoVo repo, PipelinePayload pipelinePayload, String username) {
        Pipeline pipeline = savePipelineAndTrigger(repo, pipelinePayload, username);
        handleSaveStage(pipeline, pipelinePayload);
        return pipeline;
    }

    private Pipeline updatePipelineConfig(RepoVo repo, Pipeline pipeline, PipelinePayload pipelinePayload, String username) {
        Pipeline result = updatePipelineInfo(repo, pipeline, pipelinePayload, username);
        handleUpdateStage(pipeline, pipelinePayload);
        return result;
    }

    private boolean handleSaveStage(Pipeline pipeline, PipelinePayload pipelinePayload) {
        // 代码库和匹配分支规则的配置
        List<BranchPatternConfigPayload> branchPatternConfig = pipelinePayload.getBranchPatternConfig();
        if (CollectionUtils.isNotEmpty(branchPatternConfig)) {
            branchPatternConfigService.saveBranchPatternConfig(pipeline, branchPatternConfig);
        }

        // 处理用户自定义变量
        if (CollectionUtils.isNotEmpty(pipelinePayload.getEnvConfig())) {
            variableService.saveVariable(pipeline, pipelinePayload.getEnvConfig());
        }

        // 处理通知
        if (CollectionUtils.isNotEmpty(pipelinePayload.getNoticeConfig())) {
            noticeService.saveNotice(pipeline, pipelinePayload.getNoticeConfig());
        }

        // 处理stage
        if (CollectionUtils.isNotEmpty(pipelinePayload.getStageConfig())) {
            stageService.saveStage(pipeline, pipelinePayload.getStageConfig());
        }
        return true;
    }

    private boolean handleUpdateStage(Pipeline pipeline, PipelinePayload pipelinePayload) {
        // 模块和匹配分支规则的配置
        List<BranchPatternConfigPayload> branchPatternConfig = pipelinePayload.getBranchPatternConfig();
        branchPatternConfigService.updateBranchPatternConfig(pipeline, branchPatternConfig);

        // 处理用户自定义变量
        variableService.updateVariable(pipeline, pipelinePayload.getEnvConfig());

        // 处理通知
        noticeService.updateNotice(pipeline, pipelinePayload.getNoticeConfig());

        // 处理stage
        stageService.updateStage(pipeline, pipelinePayload.getStageConfig());
        return true;
    }

    /**
     * 检查创建一条新流水线的基本条件
     */
    private void checkCreatePipelineConfig(RepoVo repo, PipelinePayload pipelinePayload) {
        checkCreatePipelineDuplicate(repo, pipelinePayload);
        checkPipelineTrigger(pipelinePayload);
        checkJobCountLimitAndUpdateReleaseJob(pipelinePayload);
    }

    private void checkUpdatePipelineConfig(RepoVo repo, Pipeline pipeline, PipelinePayload pipelinePayload) {
        checkUpdatePipelineDuplicate(repo, pipeline, pipelinePayload);
        checkPipelineTrigger(pipelinePayload);
        checkJobCountLimitAndUpdateReleaseJob(pipelinePayload);
    }

    private void checkCreatePipelineDuplicate(RepoVo repo, PipelinePayload pipelinePayload) {
        String pipelineName = pipelinePayload.getName();
        Pipeline pipeline = getPipeline(repo, pipelineName);
        if (pipeline != null) {
            throw new PipelineAlreadyExistException(pipelineName);
        }
    }

    private void checkUpdatePipelineDuplicate(RepoVo repo, Pipeline pipeline, PipelinePayload pipelinePayload) {
        String pipelineName = pipelinePayload.getName();
        Pipeline dbNamePipeline = getPipeline(repo, pipelineName);
        if (dbNamePipeline != null && !pipeline.getId().equals(dbNamePipeline.getId())) {
            throw new PipelineAlreadyExistException(pipelineName);
        }

    }

    private void checkPipelineTrigger(PipelinePayload pipelinePayload) {
        TriggerConfigPayload triggerConfig = pipelinePayload.getPipelineTriggerConfig();
        if (triggerConfig == null) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "请选择一种触发方式");
        }
    }

    private void checkJobCountLimitAndUpdateReleaseJob(PipelinePayload pipelinePayload) {
        checkStageIsEmpty(pipelinePayload);
        // 发布数量的限制
        final int jobCountLimit = 1;
        List<JobPayload> allReleaseJob = pipelinePayload.getAllReleaseJob();
        if (CollectionUtils.size(allReleaseJob) > jobCountLimit) {
            throw new OverJobCountLimitException();
        }

        List<StagePayload> stageConfigs = pipelinePayload.getStageConfig();

        for (StagePayload stageConfig : stageConfigs) {
            List<GroupJob> groupJobs = stageConfig.getGroupJobs();
            if (CollectionUtils.isEmpty(groupJobs)) {
                continue;
            }

            for (GroupJob groupJob : groupJobs) {
                for (JobPayload job : groupJob.getJobs()) {
                    JobConditionType executeType = job.getConditionType();
                    if (executeType == JobConditionType.VARIABLE_MATCH_ALL || executeType == JobConditionType.VARIABLE_MATCH_ANY || executeType == JobConditionType.VARIABLE_NOT_MATCH) {
                        ConditionTriggerType conditionTriggerType = job.getConditionTriggerType();
                        if (conditionTriggerType == null) {
                            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "程序控制执行job的策略必须为'停止'或'跳过'");
                        }
                        Set<ConditionVariablePair> conditionVariables = job.getConditionVariable();
                        if (CollectionUtils.isEmpty(conditionVariables)) {
                            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "程序控制的环境变量不允许为空");
                        }
                        for (ConditionVariablePair conditionVariable : conditionVariables) {
                            String envKey = conditionVariable.getEnvKey();
                            String envValue = conditionVariable.getEnvValue();
                            if (StringUtils.isBlank(envKey) || StringUtils.isBlank(envValue)) {
                                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "程序控制的环境变量的key或value不允许为空");
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 检查流水线的每个stage是否有job
     */
    private void checkStageIsEmpty(PipelinePayload pipelinePayload) {
        List<StagePayload> stageConfigs = pipelinePayload.getStageConfig();
        if (CollectionUtils.isEmpty(stageConfigs)) {
            throw new StageEmptyException();
        }

        for (StagePayload stageConfig : stageConfigs) {
            List<GroupJob> groupJobs = stageConfig.getGroupJobs();
            if (CollectionUtils.isEmpty(groupJobs)) {
                throw new StageJobEmptyException();
            }
        }
    }

    /**
     * 保存一条新的流水线
     */
    private Pipeline savePipelineAndTrigger(RepoVo repo, PipelinePayload pipelinePayload, String username) {
        Pipeline pipeline = new Pipeline(pipelinePayload, repo, username);
        pipelineDao.save(pipeline);
        // 处理触发器
        pipelineTriggerService.createPipelineTriggerConfig(pipeline, repo, pipelinePayload.getPipelineTriggerConfig());
        return pipeline;
    }

    /**
     * 更新流水线，如果已经存在的流水线，直接变更流水线的状态，再创建一条新的
     */
    private Pipeline updatePipelineInfo(RepoVo repo, Pipeline pipeline, PipelinePayload payload, String username) {
        pipeline.setCompanyId(repo.getCompanyId());
        pipeline.setCreateUser(username);
        pipeline.setModifyTime(new Date());

        pipeline.setName(payload.getName());
        pipeline.setJobTimeoutMinute(payload.getJobTimeoutMinute());
        pipeline.setMatchAllBranch(payload.isMatchAllBranch());
        pipeline.setResourceType(payload.getResourceType());
        pipeline.setUseDefaultCluster(payload.isUseDefaultCluster());
        pipeline.setClusterName(payload.getClusterName());

        pipelineDao.update(pipeline);

        // 处理触发器
        pipelineTriggerService.updatePipelineTriggerConfig(pipeline, repo, payload.getPipelineTriggerConfig());
        return pipeline;
    }

    @Override
    public Pipeline getByIdIfPresent(Long pipelineId) {
        Pipeline pipeline = pipelineDao.get(pipelineId);
        if (pipeline == null) {
            throw new PipelineNotExistException();
        }
        return pipeline;
    }

    @Override
    public Map<Long, Pipeline> getPipelines(RepoVo repo) {
        List<Pipeline> pipelines = pipelineDao.getByRepoKey(repo.getRepoKey());
        return convertToMap(pipelines);
    }

    @Override
    public List<Pipeline> listPipeline(RepoVo repo) {
        return pipelineDao.getByRepoKey(repo.getRepoKey());
    }

    private Map<Long, Pipeline> convertToMap(List<Pipeline> pipelines) {
        if (CollectionUtils.isEmpty(pipelines)) {
            return null;
        }
        return pipelines.stream().collect(Collectors.toMap(Pipeline::getId, pipeline -> pipeline));
    }
}
