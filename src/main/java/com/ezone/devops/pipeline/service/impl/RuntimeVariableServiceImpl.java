package com.ezone.devops.pipeline.service.impl;

import com.ezone.devops.ezcode.sdk.bean.model.InternalCommit;
import com.ezone.devops.ezcode.sdk.service.InternalCommitService;
import com.ezone.devops.pipeline.common.ScmTriggerType;
import com.ezone.devops.pipeline.dao.ReleaseVersionDao;
import com.ezone.devops.pipeline.dao.RuntimeVariableDao;
import com.ezone.devops.pipeline.enums.GlobalVariableType;
import com.ezone.devops.pipeline.model.*;
import com.ezone.devops.pipeline.service.RuntimeVariableService;
import com.ezone.devops.pipeline.service.VariableService;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.pipeline.web.request.VariablePair;
import com.ezone.ezbase.iam.service.IAMCenterService;
import com.ezone.galaxy.fasterdao.context.ShardingContext;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class RuntimeVariableServiceImpl implements RuntimeVariableService {

    @Autowired
    private VariableService variableService;
    @Autowired
    private IAMCenterService iamCenterService;
    @Autowired
    private RuntimeVariableDao runtimeVariableDao;
    @Autowired
    private ReleaseVersionDao releaseVersionDao;
    @Autowired
    private InternalCommitService commitService;

    @Override
    public Map<String, String> getAllVariables(RepoVo repo, Pipeline pipeline, PipelineRecord pipelineRecord, JobRecord jobRecord) {
        ShardingContext.putShardKey(pipeline.getId());
        String companyName = iamCenterService.getCompanyNameByCompanyId(repo.getCompanyId());

        Map<String, String> allEnvs = new HashMap<>();
        Map<String, String> customEnvs = variableService.getEnvsByPipelineId(pipeline.getId());
        if (MapUtils.isNotEmpty(customEnvs)) {
            allEnvs.putAll(customEnvs);
        }

        List<RuntimeVariable> runtimeVariables = runtimeVariableDao.getByBuildId(pipelineRecord.getId());
        if (CollectionUtils.isNotEmpty(runtimeVariables)) {
            for (RuntimeVariable runtimeVariable : runtimeVariables) {
                allEnvs.put(runtimeVariable.getEnvKey(), runtimeVariable.getEnvValue());
            }
        }

        InternalCommit commit = commitService.getCommit(repo.getCompanyId(), repo.getRepoKey(), pipelineRecord.getCommitId());
        if (commit != null && commit.getCommitTime() != null) {
            allEnvs.put(GlobalVariableType.SYS_BUILD_COMMIT_TIMESTAMP.getKey(), DateFormatUtils.format(commit.getCommitTime(), "YYYY.MM.dd-HHmmss"));
        }
        allEnvs.put(GlobalVariableType.SYS_COMPANY_NAME.getKey(), companyName);
        allEnvs.put(GlobalVariableType.SYS_BUILD_REPO.getKey(), repo.getRepoName());


        if (pipelineRecord.getScmTriggerType() == ScmTriggerType.BRANCH) {
            allEnvs.put(GlobalVariableType.SYS_BUILD_BRANCH.getKey(), pipelineRecord.getExternalName());
        } else if (pipelineRecord.getScmTriggerType() == ScmTriggerType.TAG) {
            allEnvs.put(GlobalVariableType.SYS_BUILD_TAG.getKey(), pipelineRecord.getExternalName());
        } else {
            allEnvs.put(GlobalVariableType.SYS_BUILD_COMMIT.getKey(), pipelineRecord.getExternalName());
        }

        allEnvs.put(GlobalVariableType.SYS_BUILD_COMMIT.getKey(), pipelineRecord.getCommitId());

        allEnvs.put(GlobalVariableType.SYS_PIPELINE_NAME.getKey(), pipelineRecord.getPipelineName());
        allEnvs.put(GlobalVariableType.SYS_PIPELINE_ID.getKey(), String.valueOf(pipelineRecord.getPipelineId()));
        allEnvs.put(GlobalVariableType.SYS_BUILD_RELEASE_VERSION.getKey(), pipelineRecord.getReleaseVersion());
        allEnvs.put(GlobalVariableType.SYS_BUILD_SNAPSHOT_VERSION.getKey(), pipelineRecord.getSnapshotVersion());

        ReleaseVersion releaseVersion = releaseVersionDao.getLastVersionByRepoKey(pipeline.getRepoKey());
        allEnvs.put(GlobalVariableType.SYS_LAST_RELEASE_VERSION.getKey(), releaseVersion.getVersion());

        allEnvs.put(GlobalVariableType.SYS_PIPELINE_BUILD_ID.getKey(), String.valueOf(pipelineRecord.getId()));
        allEnvs.put(GlobalVariableType.SYS_PIPELINE_BUILD_NUMBER.getKey(), String.valueOf(pipelineRecord.getBuildNumber()));
        allEnvs.put(GlobalVariableType.SYS_TRIGGER_USER.getKey(), jobRecord.getTriggerUser());

        String date = DateFormatUtils.format(pipelineRecord.getCreateTime(), "YYYYMMdd");
        String year = DateFormatUtils.format(pipelineRecord.getCreateTime(), "YYYY");
        String month = DateFormatUtils.format(pipelineRecord.getCreateTime(), "MM");
        String day = DateFormatUtils.format(pipelineRecord.getCreateTime(), "dd");

        allEnvs.put(GlobalVariableType.SYS_BUILD_DATE.getKey(), date);
        allEnvs.put(GlobalVariableType.SYS_BUILD_YEAR.getKey(), year);
        allEnvs.put(GlobalVariableType.SYS_BUILD_MONTH.getKey(), month);
        allEnvs.put(GlobalVariableType.SYS_BUILD_DAY.getKey(), day);

        return allEnvs;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean initRuntimeVariables(Pipeline pipeline, PipelineRecord pipelineRecord, Set<VariablePair> variables) {
        ShardingContext.putShardKey(pipeline.getId());
        List<Variable> existVariables = variableService.getAllVariable(pipeline);

        Set<String> existKeys = Sets.newHashSet();
        Long buildId = pipelineRecord.getId();
        List<RuntimeVariable> runtimeVariables = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(variables)) {
            for (VariablePair variable : variables) {
                String envKey = variable.getEnvKey();
                String envValue = variable.getEnvValue();
                if (StringUtils.isBlank(envKey) || StringUtils.isBlank(envValue)) {
                    continue;
                }

                RuntimeVariable runtimeVariable = new RuntimeVariable();
                runtimeVariable.setRepoKey(pipeline.getRepoKey());
                runtimeVariable.setBuildId(buildId);
                runtimeVariable.setEnvKey(envKey);
                runtimeVariable.setEnvValue(envValue);
                runtimeVariables.add(runtimeVariable);
                existKeys.add(envKey);
            }
        }

        if (CollectionUtils.isEmpty(existVariables)) {
            for (Variable variable : existVariables) {
                String envKey = variable.getEnvKey();
                if (!existKeys.contains(envKey)) {
                    RuntimeVariable runtimeVariable = new RuntimeVariable();
                    runtimeVariable.setRepoKey(pipeline.getRepoKey());
                    runtimeVariable.setBuildId(buildId);
                    runtimeVariable.setEnvValue(envKey);
                    runtimeVariable.setEnvValue(variable.getEnvValue());
                    runtimeVariables.add(runtimeVariable);
                }
            }
        }

        runtimeVariableDao.save(runtimeVariables);
        return true;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean mergeRuntimeVariables(Pipeline pipeline, PipelineRecord pipelineRecord, Set<VariablePair> variables) {
        if (CollectionUtils.isEmpty(variables)) {
            return true;
        }

        ShardingContext.putShardKey(pipeline.getId());

        Long buildId = pipelineRecord.getId();
        List<RuntimeVariable> runtimeVariables = runtimeVariableDao.getByBuildId(buildId);
        if (CollectionUtils.isEmpty(runtimeVariables)) {
            runtimeVariables = Lists.newArrayListWithCapacity(variables.size());
            for (VariablePair variable : variables) {
                RuntimeVariable runtimeVariable = new RuntimeVariable();
                runtimeVariable.setRepoKey(pipeline.getRepoKey());
                runtimeVariable.setBuildId(buildId);
                runtimeVariable.setEnvKey(variable.getEnvKey());
                runtimeVariable.setEnvValue(variable.getEnvValue());
                runtimeVariables.add(runtimeVariable);
            }

            runtimeVariableDao.save(runtimeVariables);
        } else {
            List<RuntimeVariable> addRuntimeVariables = Lists.newArrayListWithCapacity(variables.size());
            List<RuntimeVariable> updateRuntimeVariables = Lists.newArrayListWithCapacity(variables.size());

            Set<String> allEnvKey = new HashSet<>();
            Map<String, RuntimeVariable> existRuntimeVariableMap = Maps.newHashMap();

            for (RuntimeVariable runtimeVariable : runtimeVariables) {
                allEnvKey.add(runtimeVariable.getEnvKey());
                existRuntimeVariableMap.put(runtimeVariable.getEnvKey(), runtimeVariable);
            }

            for (VariablePair variable : variables) {
                String envKey = variable.getEnvKey();
                String envValue = variable.getEnvValue();
                if (existRuntimeVariableMap.containsKey(envKey)) {
                    RuntimeVariable runtimeVariable = existRuntimeVariableMap.get(envKey);
                    runtimeVariable.setEnvValue(envValue);
                    updateRuntimeVariables.add(runtimeVariable);
                } else {
                    if (allEnvKey.contains(envKey)) {
                        continue;
                    }
                    RuntimeVariable runtimeVariable = new RuntimeVariable();
                    runtimeVariable.setRepoKey(pipeline.getRepoKey());
                    runtimeVariable.setBuildId(buildId);
                    runtimeVariable.setEnvKey(envKey);
                    runtimeVariable.setEnvValue(envValue);
                    addRuntimeVariables.add(runtimeVariable);

                    allEnvKey.add(envKey);
                }
            }

//            Collection<RuntimeVariable> deleteRuntimeVariables = existRuntimeVariableMap.values();
//            if (CollectionUtils.isNotEmpty(deleteRuntimeVariables)) {
//                runtimeVariableDao.delete(deleteRuntimeVariables);
//            }

            if (CollectionUtils.isNotEmpty(addRuntimeVariables)) {
                runtimeVariableDao.save(addRuntimeVariables);
            }

            if (CollectionUtils.isNotEmpty(updateRuntimeVariables)) {
                runtimeVariableDao.update(updateRuntimeVariables);
            }
        }

        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteRuntimeVariables(PipelineRecord pipelineRecord) {
        ShardingContext.putShardKey(pipelineRecord.getPipelineId());
        return runtimeVariableDao.deleteByBuildId(pipelineRecord.getId()) > 0;
    }
}
