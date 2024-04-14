package com.ezone.devops.plugins.job.other.callpipeline.service.impl;

import com.ezone.devops.plugins.job.other.callpipeline.dao.CallPipelineConfigDao;
import com.ezone.devops.plugins.job.other.callpipeline.model.CallPipelineConfig;
import com.ezone.devops.plugins.job.other.callpipeline.service.CallPipelineConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CallPipelineConfigServiceImpl implements CallPipelineConfigService {

    @Autowired
    private CallPipelineConfigDao callPipelineConfigDao;

    @Override
    public boolean deleteCallPipelineConfig(Long id) {
        return callPipelineConfigDao.delete(id);
    }

    @Override
    public CallPipelineConfig getById(Long id) {
        return callPipelineConfigDao.get(id);
    }

    @Override
    public CallPipelineConfig saveCallPipelineConfig(CallPipelineConfig callPipelineConfig) {
        return callPipelineConfigDao.save(callPipelineConfig);
    }
}