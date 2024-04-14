package com.ezone.devops.plugins.job.other.callpipeline;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.exception.InitialJobException;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.plugins.job.PipelineInitContext;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.other.callpipeline.model.CallPipelineBuild;
import com.ezone.devops.plugins.job.other.callpipeline.model.CallPipelineConfig;
import com.ezone.devops.plugins.job.other.callpipeline.service.CallPipelineBuildService;
import com.ezone.devops.plugins.job.other.callpipeline.service.CallPipelineConfigService;
import com.ezone.galaxy.framework.common.bean.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CallPipelineDataOperator implements PluginDataOperator<CallPipelineConfig, CallPipelineBuild> {

    @Autowired
    private CallPipelineBuildService callPipelineBuildService;
    @Autowired
    private CallPipelineConfigService callPipelineConfigService;

    @Override
    public boolean checkJob(Job job, JSONObject json) {
        CallPipelineConfig callPipelineConfig = json.toJavaObject(CallPipelineConfig.class);

        if (StringUtils.isBlank(callPipelineConfig.getRepoName())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的代码库名称不能为空");
        }

        if (StringUtils.isBlank(callPipelineConfig.getPipelineName())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的流水线名称不能为空");
        }

        if (StringUtils.isBlank(callPipelineConfig.getBranchName())) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), job.getName() + "的分支名称不能为空");
        }

        return true;
    }

    @Override
    public Long saveRealJob(String jobType, JSONObject json) {
        CallPipelineConfig callPipelineConfig = json.toJavaObject(CallPipelineConfig.class);
        callPipelineConfig = callPipelineConfigService.saveCallPipelineConfig(callPipelineConfig);
        return callPipelineConfig.getId();
    }

    @Override
    public boolean deleteRealJob(Long id) {
        return callPipelineConfigService.deleteCallPipelineConfig(id);
    }

    @Override
    public boolean updateRealJobRecord(Long realJobRecordId, JSONObject json) {
        CallPipelineBuild callPipelineBuild = json.toJavaObject(CallPipelineBuild.class);
        return callPipelineBuildService.updateCallPipelineBuild(callPipelineBuild);
    }

    @Override
    public CallPipelineConfig getRealJob(Long id) {
        return callPipelineConfigService.getById(id);
    }

    @Override
    public CallPipelineBuild getRealJobRecord(Long id) {
        return callPipelineBuildService.getById(id);
    }

    @Override
    public Long initRealJobRecordByRealJobId(Long realJobConfigId, PipelineInitContext context) {
        CallPipelineConfig callPipelineConfig = getRealJob(realJobConfigId);
        if (null == callPipelineConfig) {
            log.error("init job error, config not exist,realJobConfigId:[{}]", realJobConfigId);
            throw new InitialJobException();
        }

        CallPipelineBuild callPipelineBuild = new CallPipelineBuild();
        callPipelineBuild = callPipelineBuildService.saveCallPipelineBuild(callPipelineBuild);
        log.debug("save build:[{}]", callPipelineBuild);
        return callPipelineBuild.getId();
    }
}
