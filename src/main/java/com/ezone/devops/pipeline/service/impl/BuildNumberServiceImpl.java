package com.ezone.devops.pipeline.service.impl;

import com.ezone.devops.pipeline.dao.PipelineRecordDao;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.service.BuildNumberService;
import com.ezone.galaxy.framework.redis.annotation.KLock;
import com.ezone.galaxy.framework.redis.enums.LockTimeoutStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuildNumberServiceImpl implements BuildNumberService {

    @Autowired
    private PipelineRecordDao pipelineRecordDao;

    @KLock(name = "next_build_number", keys = "#pipeline.id", leaseTime = 500, lockTimeoutStrategy = LockTimeoutStrategy.KEEP_ACQUIRE)
    @Override
    public Long getNextBuildNumber(Pipeline pipeline) {
        Long pipelineId = pipeline.getId();
        Long buildNumber = pipelineRecordDao.findMaxBuildNumberByPipelineId(pipelineId);
        if (buildNumber == null || buildNumber <= 0) {
            buildNumber = 0L;
        }
        buildNumber += 1L;
        return buildNumber;
    }
}
