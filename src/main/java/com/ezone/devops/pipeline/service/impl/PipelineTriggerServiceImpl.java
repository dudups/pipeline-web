package com.ezone.devops.pipeline.service.impl;

import com.ezone.devops.pipeline.clients.CrontabClient;
import com.ezone.devops.pipeline.common.TriggerMode;
import com.ezone.devops.pipeline.dao.PipelineTriggerConfigDao;
import com.ezone.devops.pipeline.exception.CommonException;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineTriggerConfig;
import com.ezone.devops.pipeline.service.PipelineTriggerService;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.pipeline.web.request.CrontabPayload;
import com.ezone.devops.pipeline.web.request.TriggerConfigPayload;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 定时任务相关服务.
 */
@Slf4j
@Service
public class PipelineTriggerServiceImpl implements PipelineTriggerService {

    @Autowired
    private CrontabClient crontabClient;
    @Autowired
    private PipelineTriggerConfigDao pipelineTriggerConfigDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PipelineTriggerConfig createPipelineTriggerConfig(Pipeline pipeline, RepoVo repo,
                                                             TriggerConfigPayload triggerConfigPayload) {
        PipelineTriggerConfig pipelineTriggerConfig = new PipelineTriggerConfig(pipeline, triggerConfigPayload);
        pipelineTriggerConfigDao.save(pipelineTriggerConfig);
        registerCrontab(pipeline, repo, triggerConfigPayload);
        return pipelineTriggerConfig;
    }

    private void registerCrontab(Pipeline pipeline, RepoVo repo, TriggerConfigPayload triggerConfigPayload) {
        if (triggerConfigPayload.getTriggerMode() != TriggerMode.CRONTAB) {
            return;
        }
        CrontabPayload crontabPayload = triggerConfigPayload.getCrontab();
        log.info("start register crontab:[{}], pipeline:[{}]", crontabPayload, pipeline);
        BaseResponse<?> response = crontabClient.registerCrontab(pipeline, crontabPayload);
        if (response.isError()) {
            log.error("create crontab failed pipeline:[{}],repo:[{}]", pipeline, repo);
            throw new CommonException(response);
        }
        log.info("finished register crontab:[{}], pipeline:[{}]", crontabPayload, pipeline);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PipelineTriggerConfig updatePipelineTriggerConfig(Pipeline pipeline,
                                                             RepoVo repo,
                                                             TriggerConfigPayload payload) {
        PipelineTriggerConfig triggerConfig = pipelineTriggerConfigDao.getByPipelineId(pipeline.getId());
        if (triggerConfig != null) {
            // 删除旧的
            pipelineTriggerConfigDao.delete(triggerConfig);
            if (triggerConfig.getTriggerMode() == TriggerMode.CRONTAB) {
                deletePipelineCrontabConfig(pipeline, repo, triggerConfig);
            }
        }

        return createPipelineTriggerConfig(pipeline, repo, payload);
    }

    @Override
    public TriggerConfigPayload getTriggerConfigPayload(Pipeline pipeline) {
        PipelineTriggerConfig pipelineTriggerConfig = pipelineTriggerConfigDao.getByPipelineId(pipeline.getId());
        if (pipelineTriggerConfig == null) {
            return null;
        }
        return new TriggerConfigPayload(pipelineTriggerConfig);
    }

    @Override
    public void deletePipelineCrontabConfig(Pipeline pipeline, RepoVo repo) {
        PipelineTriggerConfig crontabConfig = pipelineTriggerConfigDao.getByPipelineId(pipeline.getId());
        deletePipelineCrontabConfig(pipeline, repo, crontabConfig);
    }

    private void deletePipelineCrontabConfig(Pipeline pipeline, RepoVo repo,
                                             PipelineTriggerConfig crontabConfig) {
        if (crontabConfig != null && crontabConfig.getTriggerMode() == TriggerMode.CRONTAB) {
            BaseResponse<?> response = crontabClient.deletePipelineCrontab(pipeline);
            if (response.isError()) {
                log.error("delete crontab trigger error:[{}]", crontabConfig);
            }
        }
        pipelineTriggerConfigDao.delete(crontabConfig);
    }
}
