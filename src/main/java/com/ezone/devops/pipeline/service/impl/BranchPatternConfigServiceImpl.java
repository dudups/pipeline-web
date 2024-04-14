package com.ezone.devops.pipeline.service.impl;

import com.ezone.devops.ezcode.sdk.bean.model.InternalBranch;
import com.ezone.devops.ezcode.sdk.service.InternalBranchService;
import com.ezone.devops.pipeline.dao.BranchPatternConfigDao;
import com.ezone.devops.pipeline.enums.BranchMatchType;
import com.ezone.devops.pipeline.model.BranchPatternConfig;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.service.BranchPatternConfigService;
import com.ezone.devops.pipeline.service.PipelineService;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.pipeline.web.request.BranchPatternConfigPayload;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BranchPatternConfigServiceImpl implements BranchPatternConfigService {

    @Autowired
    private InternalBranchService branchService;
    @Autowired
    private PipelineService pipelineService;
    @Autowired
    private BranchPatternConfigDao branchPatternConfigDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveBranchPatternConfig(Pipeline pipeline,
                                           List<BranchPatternConfigPayload> payloads) {
        if (CollectionUtils.isEmpty(payloads)) {
            return false;
        }
        log.info("save branch pattern pipeline config:[{}], pattern config:[{}]", pipeline, payloads);
        List<BranchPatternConfig> branchPatternConfigs = payloads.stream()
                .map(payload -> new BranchPatternConfig(pipeline, payload))
                .collect(Collectors.toList());
        return branchPatternConfigDao.save(branchPatternConfigs) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateBranchPatternConfig(Pipeline pipeline,
                                             List<BranchPatternConfigPayload> payloads) {
        branchPatternConfigDao.deleteBranchPatternByPipelineId(pipeline.getId());
        if (CollectionUtils.isEmpty(payloads)) {
            return false;
        }

        List<BranchPatternConfig> branchPatternConfigs = payloads.stream()
                .map(payload -> new BranchPatternConfig(pipeline, payload)).collect(Collectors.toList());
        return branchPatternConfigDao.save(branchPatternConfigs) > 0;
    }

    @Override
    public List<BranchPatternConfig> getBranchPattern(Pipeline pipelineConfig) {
        return getByPipelineIds(Lists.newArrayList(pipelineConfig.getId()));
    }

    @Override
    public List<BranchPatternConfigPayload> getBranchPatternPayload(Pipeline pipeline) {
        List<BranchPatternConfig> branchPatternConfigs = getBranchPattern(pipeline);
        if (CollectionUtils.isEmpty(branchPatternConfigs)) {
            return null;
        }

        List<BranchPatternConfigPayload> branchPatternConfigPayloads =
                Lists.newArrayListWithCapacity(branchPatternConfigs.size());
        for (BranchPatternConfig branchPatternConfig : branchPatternConfigs) {
            BranchPatternConfigPayload branchPatternConfigPayload = new BranchPatternConfigPayload();
            BeanUtils.copyProperties(branchPatternConfig, branchPatternConfigPayload);
            branchPatternConfigPayloads.add(branchPatternConfigPayload);
        }
        return branchPatternConfigPayloads;
    }

    @Override
    public List<BranchPatternConfig> getByPipelineIds(Collection<Long> pipelineIds) {
        return branchPatternConfigDao.getByPipelineIds(pipelineIds);
    }

    @Override
    public List<InternalBranch> getMatchedBranches(RepoVo repo, Pipeline pipeline) {
        List<InternalBranch> branches = branchService.listBranches(repo.getCompanyId(), repo.getRepoKey());
        if (CollectionUtils.isEmpty(branches) || pipeline.isMatchAllBranch()) {
            return branches;
        }
        List<BranchPatternConfig> branchPatternConfigs = getBranchPattern(pipeline);
        if (CollectionUtils.isEmpty(branchPatternConfigs)) {
            return Collections.emptyList();
        }
        Map<String, InternalBranch> matchedBranchMap = Maps.newHashMap();
        for (BranchPatternConfig patternConfig : branchPatternConfigs) {
            for (InternalBranch branch : branches) {
                if (patternConfig.getMatchType().match(patternConfig.getPattern(), branch.getName())) {
                    matchedBranchMap.put(branch.getName(), branch);
                }
            }
        }
        return Lists.newArrayList(matchedBranchMap.values());
    }

    @Override
    public Set<Pipeline> getMatchedPipelines(RepoVo repo, String branchName) {
        Map<Long, Pipeline> pipelines = pipelineService.getPipelines(repo);
        if (MapUtils.isEmpty(pipelines)) {
            return Collections.emptySet();
        }
        Set<Long> pipelineIds = pipelines.keySet();
        List<BranchPatternConfig> branchPatternConfigs = branchPatternConfigDao.getByPipelineIds(pipelineIds);
        Map<Long, List<BranchPatternConfig>> patternMap = convertToMap(branchPatternConfigs);

        Set<Pipeline> matchPipelines = Sets.newHashSetWithExpectedSize(pipelines.size());
        pipelines.values().forEach(pipeline -> {
            // 匹配所有分支的流水线
            if (pipeline.isMatchAllBranch()) {
                matchPipelines.add(pipeline);
                return;
            }
            List<BranchPatternConfig> patternConfigs = patternMap.get(pipeline.getId());
            if (CollectionUtils.isEmpty(patternConfigs)) {
                return;
            }
            for (BranchPatternConfig patternConfig : patternConfigs) {
                BranchMatchType matchType = patternConfig.getMatchType();
                if (matchType.match(patternConfig.getPattern(), branchName)) {
                    matchPipelines.add(pipeline);
                    break;
                }
            }
        });
        return matchPipelines;
    }

    private Map<Long, List<BranchPatternConfig>> convertToMap(List<BranchPatternConfig> branchPatternConfigs) {
        Map<Long, List<BranchPatternConfig>> relation = Maps.newHashMap();
        if (CollectionUtils.isEmpty(branchPatternConfigs)) {
            return relation;
        }
        for (BranchPatternConfig branchPatternConfig : branchPatternConfigs) {
            final Long pipelineId = branchPatternConfig.getPipelineId();
            if (relation.containsKey(pipelineId)) {
                relation.get(pipelineId).add(branchPatternConfig);
            } else {
                relation.put(pipelineId, Lists.newArrayList(branchPatternConfig));
            }
        }
        return relation;
    }

}
