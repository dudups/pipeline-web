package com.ezone.devops.pipeline.service.impl;

import com.ezone.devops.pipeline.dao.JobTemplateDao;
import com.ezone.devops.pipeline.dao.PipelineTemplateDao;
import com.ezone.devops.pipeline.dao.StageTemplateDao;
import com.ezone.devops.pipeline.exception.OverJobCountLimitException;
import com.ezone.devops.pipeline.exception.StageEmptyException;
import com.ezone.devops.pipeline.exception.StageJobEmptyException;
import com.ezone.devops.pipeline.model.JobTemplate;
import com.ezone.devops.pipeline.model.PipelineTemplate;
import com.ezone.devops.pipeline.model.StageTemplate;
import com.ezone.devops.pipeline.service.PipelineTemplateService;
import com.ezone.devops.pipeline.web.request.JobTemplatePayload;
import com.ezone.devops.pipeline.web.request.PipelineTemplatePayload;
import com.ezone.devops.pipeline.web.request.StageTemplatePayload;
import com.ezone.galaxy.framework.common.bean.BaseException;
import com.ezone.galaxy.framework.common.bean.PageResult;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class PipelineTemplateServiceImpl implements PipelineTemplateService {

    private static final Long HEAD_ID = 0L;

    @Autowired
    private PipelineTemplateDao pipelineTemplateDao;
    @Autowired
    private StageTemplateDao stageTemplateDao;
    @Autowired
    private JobTemplateDao jobTemplateDao;

    @Override
    public PageResult<List<PipelineTemplatePayload>> suggestTemplate(Long companyId, String name, int pageNumber, int pageSize) {
        PageResult<List<PipelineTemplatePayload>> results = new PageResult<>();

        PageResult<List<PipelineTemplate>> datas = pipelineTemplateDao.suggestByName(companyId, name, pageNumber, pageSize);
        if (CollectionUtils.isEmpty(datas.getItems())) {
            return results;
        }

        Set<Long> ids = new HashSet<>();
        for (PipelineTemplate pipelineTemplate : datas.getItems()) {
            ids.add(pipelineTemplate.getId());
        }

        List<StageTemplate> stageTemplates = stageTemplateDao.getByTemplateIds(ids);
        if (CollectionUtils.isEmpty(stageTemplates)) {
            return results;
        }

        Map<Long, List<StageTemplate>> listMap = new HashMap<>();
        for (StageTemplate stageTemplate : stageTemplates) {
            Long templateId = stageTemplate.getTemplateId();
            if (listMap.containsKey(templateId)) {
                listMap.get(templateId).add(stageTemplate);
            } else {
                listMap.put(templateId, Lists.newArrayList(stageTemplate));
            }
        }

        List<PipelineTemplatePayload> items = new ArrayList<>();
        for (PipelineTemplate pipelineTemplate : datas.getItems()) {
            items.add(new PipelineTemplatePayload(pipelineTemplate, listMap.get(pipelineTemplate.getId()), null));
        }

        results.setTotal(datas.getTotal());
        results.setItems(items);
        return results;
    }

    @Override
    public PipelineTemplatePayload getTemplate(Long companyId, Long id) {
        PipelineTemplate pipelineTemplate = pipelineTemplateDao.get(id);
        if (pipelineTemplate == null) {
            throw new BaseException(HttpStatus.NOT_FOUND.value(), "模板不存在");
        }

        Long templateId = pipelineTemplate.getId();
        List<StageTemplate> stageTemplates = stageTemplateDao.getByTemplateId(templateId);
        List<JobTemplate> jobTemplates = jobTemplateDao.getByTemplateId(templateId);

        Map<Long, List<List<JobTemplate>>> groups = groupByStageId(jobTemplates);
        return new PipelineTemplatePayload(pipelineTemplate, stageTemplates, groups);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveTemplate(Long companyId, String createUser, PipelineTemplatePayload payload) {
        PipelineTemplate pipelineTemplate = pipelineTemplateDao.getByName(companyId, payload.getName());
        if (pipelineTemplate != null) {
            throw new BaseException(HttpStatus.CONTINUE.value(), "同名模板已经存在");
        }

        checkReleaseJob(payload);

        pipelineTemplate = new PipelineTemplate(companyId, createUser, payload);
        pipelineTemplateDao.save(pipelineTemplate);
        saveStages(pipelineTemplate, payload.getStages());
        return true;
    }

    private void saveStages(PipelineTemplate pipelineTemplate, List<StageTemplatePayload> stages) {
        if (CollectionUtils.isEmpty(stages)) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "stage is null");
        }

        Long upstreamId = HEAD_ID;
        for (StageTemplatePayload stage : stages) {
            StageTemplate stageTemplate = new StageTemplate(pipelineTemplate, stage, upstreamId);
            stageTemplateDao.save(stageTemplate);
            saveJobs(pipelineTemplate, stageTemplate, stage.getGroupJobs());
            upstreamId = stageTemplate.getId();
        }
    }

    private void saveJobs(PipelineTemplate pipelineTemplate, StageTemplate stageTemplate, List<List<JobTemplatePayload>> groupJobs) {
        if (CollectionUtils.isEmpty(groupJobs)) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "job is null");
        }

        for (List<JobTemplatePayload> groupJob : groupJobs) {
            Long upstreamId = HEAD_ID;
            for (JobTemplatePayload job : groupJob) {
                JobTemplate jobTemplate = new JobTemplate(pipelineTemplate, stageTemplate, job, upstreamId);
                jobTemplateDao.save(jobTemplate);
                upstreamId = jobTemplate.getId();
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateTemplate(Long companyId, Long id, String updater, PipelineTemplatePayload payload) {
        PipelineTemplate pipelineTemplate = pipelineTemplateDao.get(id);
        if (pipelineTemplate == null) {
            throw new BaseException(HttpStatus.NOT_FOUND.value(), "模板不存在");
        }

        String name = payload.getName();
        PipelineTemplate dbPipelineTemplate = pipelineTemplateDao.getByName(companyId, name);
        if (dbPipelineTemplate != null && !dbPipelineTemplate.getId().equals(id)) {
            throw new BaseException(HttpStatus.CONFLICT.value(), "同名模板已经存在");
        }

        checkReleaseJob(payload);

        pipelineTemplate.setName(name);
        pipelineTemplate.setCreateUser(updater);
        pipelineTemplate.setModifyTime(new Date());
        pipelineTemplateDao.update(pipelineTemplate);

        Long templateId = pipelineTemplate.getId();
        stageTemplateDao.deleteByTemplateId(templateId);
        jobTemplateDao.deleteByTemplateId(templateId);

        saveStages(pipelineTemplate, payload.getStages());
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteTemplate(Long companyId, Long id) {
        PipelineTemplate pipelineTemplate = pipelineTemplateDao.get(id);
        if (pipelineTemplate == null) {
            throw new BaseException(HttpStatus.NOT_FOUND.value(), "模板不存在");
        }

        Long templateId = pipelineTemplate.getId();
        pipelineTemplateDao.delete(templateId);
        stageTemplateDao.deleteByTemplateId(templateId);
        jobTemplateDao.deleteByTemplateId(templateId);
        return true;
    }

    private void checkReleaseJob(PipelineTemplatePayload payload) {
        checkStageIsEmpty(payload);
        // 发布数量的限制
        final int jobCountLimit = 1;
        List<JobTemplatePayload> allReleaseJob = payload.getAllReleaseJob();
        if (CollectionUtils.size(allReleaseJob) > jobCountLimit) {
            throw new OverJobCountLimitException();
        }
    }

    private void checkStageIsEmpty(PipelineTemplatePayload payload) {
        List<StageTemplatePayload> stageConfigs = payload.getStages();
        if (CollectionUtils.isEmpty(stageConfigs)) {
            throw new StageEmptyException();
        }

        for (StageTemplatePayload templatePayload : stageConfigs) {
            List<List<JobTemplatePayload>> groupJobs = templatePayload.getGroupJobs();
            if (CollectionUtils.isEmpty(groupJobs)) {
                throw new StageJobEmptyException();
            }
        }
    }

    /**
     * 按照阶段id分组
     *
     * @param allJobTemplates
     * @return
     */
    private Map<Long, List<List<JobTemplate>>> groupByStageId(List<JobTemplate> allJobTemplates) {
        Map<Long, List<List<JobTemplate>>> stageGroups = new HashMap<>();

        if (CollectionUtils.isEmpty(allJobTemplates)) {
            return stageGroups;
        }

        // key: stageId
        Map<Long, List<JobTemplate>> stageJobGroups = new HashMap<>();
        for (JobTemplate jobTemplate : allJobTemplates) {
            Long stageId = jobTemplate.getStageId();
            if (stageJobGroups.containsKey(stageId)) {
                stageJobGroups.get(stageId).add(jobTemplate);
            } else {
                stageJobGroups.put(stageId, Lists.newArrayList(Lists.newArrayList(jobTemplate)));
            }
        }

        Map<Long, List<List<JobTemplate>>> allGroups = Maps.newHashMap();
        for (Map.Entry<Long, List<JobTemplate>> entry : stageJobGroups.entrySet()) {
            Long stageId = entry.getKey();
            List<List<JobTemplate>> jobPipelines = Lists.newArrayList();

            // 存储<upstreamId, JobTemplate>的键值对，用于获取上下游关系
            Map<Long, JobTemplate> jobMaps = Maps.newHashMap();
            for (JobTemplate jobTemplate : entry.getValue()) {
                if (HEAD_ID.equals(jobTemplate.getUpstreamId())) {
                    jobPipelines.add(Lists.newArrayList(Lists.newArrayList(jobTemplate)));
                } else {
                    jobMaps.put(jobTemplate.getUpstreamId(), jobTemplate);
                }
            }

            // 按照开始job组装每条job线
            for (List<JobTemplate> jobTemplates : jobPipelines) {
                JobTemplate jobTemplate = jobTemplates.get(0);
                Long upstreamId = jobTemplate.getId();
                for (int i = 0; i < entry.getValue().size(); i++) {
                    JobTemplate subJobTemplate = jobMaps.get(upstreamId);
                    // 到达此条线末尾，break
                    if (null == subJobTemplate) {
                        allGroups.put(stageId, jobPipelines);
                        break;
                    }

                    upstreamId = subJobTemplate.getId();
                    jobTemplates.add(subJobTemplate);
                }
            }
        }
        return allGroups;
    }
}