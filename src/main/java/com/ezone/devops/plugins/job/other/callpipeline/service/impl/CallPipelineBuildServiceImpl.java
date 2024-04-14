package com.ezone.devops.plugins.job.other.callpipeline.service.impl;

import com.ezone.devops.plugins.job.other.callpipeline.dao.CallPipelineBuildDao;
import com.ezone.devops.plugins.job.other.callpipeline.model.CallPipelineBuild;
import com.ezone.devops.plugins.job.other.callpipeline.service.CallPipelineBuildService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CallPipelineBuildServiceImpl implements CallPipelineBuildService {

    @Autowired
    private CallPipelineBuildDao callPipelineBuildDao;

    @Override
    public boolean updateCallPipelineBuild(CallPipelineBuild callPipelineBuild) {
        return callPipelineBuildDao.update(callPipelineBuild);
    }

    @Override
    public CallPipelineBuild getById(Long id) {
        return callPipelineBuildDao.get(id);
    }

    @Override
    public CallPipelineBuild saveCallPipelineBuild(CallPipelineBuild callPipelineBuild) {
        return callPipelineBuildDao.save(callPipelineBuild);
    }
}
