package com.ezone.devops.plugins.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.clients.CrontabClient;
import com.ezone.devops.pipeline.common.BuildStatus;
import com.ezone.devops.pipeline.common.JobEvent;
import com.ezone.devops.pipeline.exception.PluginNotExistException;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.pipeline.rocketmq.producer.ResourceChargeProducer;
import com.ezone.devops.pipeline.service.JobRecordService;
import com.ezone.devops.pipeline.service.PipelineRecordService;
import com.ezone.devops.pipeline.service.PipelineService;
import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginDataOperator;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.job.PluginOperator;
import com.ezone.devops.plugins.service.PluginService;
import com.ezone.devops.plugins.web.request.PluginCallbackPayload;
import com.ezone.ezbase.iam.bean.enums.BillingItem;
import com.ezone.galaxy.framework.common.bean.BaseException;
import com.ezone.galaxy.framework.common.util.JsonUtils;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PluginServiceImpl implements PluginService {

    private static final String PRIMARY_KEY = "id";
    private final Map<String, Plugin> ALL_PLUGINS = Maps.newHashMap();
    private static final String JOB_ID = "jobId";

    @Autowired
    private PipelineService pipelineService;
    @Autowired
    private ResourceChargeProducer resourceChargeProducer;
    @Autowired
    private JobRecordService jobRecordService;
    @Autowired
    private PipelineRecordService pipelineRecordService;
    @Autowired
    private CrontabClient crontabClient;

    @Autowired
    public void initJobPlugins(List<Plugin> plugins) {
        for (Plugin plugin : plugins) {
            PluginInfo pluginInfo = plugin.getPluginInfo();
            String jobType = pluginInfo.getJobType();

            if (ALL_PLUGINS.containsKey(jobType)) {
                throw new BaseException(HttpStatus.CONFLICT.value(), "插件" + jobType + "已经存在");
            }
            ALL_PLUGINS.put(jobType, plugin);
        }
    }

    @Override
    public Plugin getPlugin(String jobType) {
        if (StringUtils.isBlank(jobType)) {
            return null;
        }
        return ALL_PLUGINS.get(jobType);
    }

    @Override
    public List<PluginInfo> listPlugins() {
        return ALL_PLUGINS.values().stream().map(Plugin::getPluginInfo).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long savePluginConfig(Job job, JSONObject json) {
        String jobType = job.getJobType();
        Plugin plugin = getPlugin(jobType);
        if (null != plugin) {
            PluginDataOperator<?, ?> pluginDataOperator = plugin.getPluginDataOperator();
            // 校验插件字段是否非法
            if (!pluginDataOperator.checkJob(job, json)) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "插件" + jobType + "字段非法");
            }
            json.remove("id");
            return pluginDataOperator.saveRealJob(jobType, json);
        }
        return 0L;
    }

    @Override
    public JSONObject getPluginConfig(Job job) {
        String jobType = job.getJobType();
        Plugin plugin = getPlugin(jobType);
        if (plugin == null) {
            throw new PluginNotExistException(jobType);
        }

        Object object = plugin.getPluginDataOperator().getRealJob(job.getPluginId());
        if (object == null) {
            return null;
        }
        String jsonJob = JSONObject.toJSONString(object);
        JSONObject jsonObject = JSONObject.parseObject(jsonJob);
        if (jsonObject != null) {
            if(plugin.getPluginInfo().getJobType().equals(jobType)){
                jsonObject.put(JOB_ID,job.getId());
            }
            jsonObject.remove(PRIMARY_KEY);
        }
        return jsonObject;
    }

    @Override
    public JSONObject getPluginBuild(JobRecord jobRecord) {
        Plugin plugin = getPlugin(jobRecord);
        Object realJobBuild = plugin.getPluginDataOperator().getRealJobRecord(jobRecord.getPluginRecordId());
        if (realJobBuild == null) {
            return null;
        }
        return (JSONObject) JSONObject.toJSON(realJobBuild);
    }

    @Override
    public boolean updatePluginBuild(JobRecord jobRecord, JSONObject updateFields) {
        Plugin plugin = getPlugin(jobRecord);
        plugin.getPluginDataOperator().updateRealJobBuildWithFields(jobRecord.getPluginRecordId(), updateFields);
        jobRecordService.updateJobRecord(jobRecord);
        return true;
    }

    @Override
    public boolean cancelPluginBuild(PipelineRecord pipelineRecord, JobRecord jobRecord, String triggerUser) {
        if (BuildStatus.isEnd(jobRecord.getStatus())) {
            return false;
        }
        Plugin plugin = getPlugin(jobRecord);
        if (plugin.getPluginOperator() == null) {
            throw new BaseException(HttpStatus.NOT_FOUND.value(), "插件执行器不存在");
        }

        log.info("trigger cancel job start, jobId:[{}]", jobRecord.getId());
        plugin.getPluginOperator().asyncCancel(pipelineRecord, jobRecord, triggerUser);
        log.info("trigger cancel job success, jobId:[{}]", jobRecord.getId());

        crontabClient.deleteJobTimeout(jobRecord);
        Pipeline pipeline = pipelineService.getByIdIfPresent(jobRecord.getPipelineId());
        sendCharge(pipeline, jobRecord);
        return true;
    }

    @Override
    public boolean handlePluginBuildCallback(Long pipelineId, Long jobId, PluginCallbackPayload payload) {
        JobEvent jobEvent = payload.getJobEvent();
        String message = payload.getMessage();
        log.info("plugin callback pipelineId:[{}], jobId:[{}], jobEvent:[{}], message:[{}]",
                pipelineId, jobId, jobEvent, message);
        Pipeline pipeline = pipelineService.getByIdIfPresent(pipelineId);

        JobRecord jobRecord = jobRecordService.getByExternalJobIdIfPresent(jobId);
        PipelineRecord pipelineRecord = pipelineRecordService.getByIdIfPresent(jobRecord.getPipelineRecordId());
        if (jobRecord.getStatus() == BuildStatus.SUCCESS) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "任务已经结束");
        }

        Object data = payload.getData();
        if (data instanceof Collection) {
            JSONArray jsonArray = JSONArray.parseArray(JsonUtils.toJson(data));
            processCallback(pipelineRecord, jobRecord, jobEvent, message, jsonArray);
        } else {
            JSONObject jsonObject = JSONObject.parseObject(JsonUtils.toJson(data));
            if (BuildStatus.isRunning(jobRecord.getStatus()) && jobEvent == JobEvent.UPDATE) {
                log.info("receive running job callback, jobRecord:[{}], jobEvent:[{}], data:[{}]",
                        jobRecord.getId(), jobEvent, jsonObject);
                if (data != null) {
                    // 更新插件的字段
                    log.info("update real job build, jobRecord:[{}] ,data:[{}]", jobRecord.getId(), data);
                    updatePluginBuild(jobRecord, jsonObject);
                }
                return true;
            }

            processCallback(pipelineRecord, jobRecord, jobEvent, message, jsonObject);
        }

        if (JobEvent.isEnd(jobEvent)) {
            sendCharge(pipeline, jobRecord);
        }

        return true;
    }


    private void processCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, JobEvent jobEvent, String message, JSONObject data) {
        // 执行成功回调
        log.info("job finished callback,job id:[{}], jobEvent:[{}], data:[{}]", jobRecord.getId(), jobEvent, data);
        PluginOperator pluginOperator = getPlugin(jobRecord.getJobType()).getPluginOperator();
        switch (jobEvent) {
            case SUCCESS: {
                pluginOperator.jobSuccessCallback(pipelineRecord, jobRecord, message, data);
                break;
            }
            case FAIL: {
                pluginOperator.jobFailedCallback(pipelineRecord, jobRecord, message, data);
                break;
            }
            case ABORT: {
                pluginOperator.jobAbortCallback(pipelineRecord, jobRecord, message, data);
                break;
            }
            case CANCEL: {
                pluginOperator.jobCancelCallback(pipelineRecord, jobRecord, message);
                break;
            }
        }
    }

    private void processCallback(PipelineRecord pipelineRecord, JobRecord jobRecord, JobEvent jobEvent, String message, JSONArray data) {
        // 执行成功回调
        log.info("job finished callback,job id:[{}], jobEvent:[{}], data:[{}]", jobRecord.getId(), jobEvent, data);
        PluginOperator pluginOperator = getPlugin(jobRecord.getJobType()).getPluginOperator();
        switch (jobEvent) {
            case SUCCESS: {
                pluginOperator.jobSuccessCallback(pipelineRecord, jobRecord, message, data);
                break;
            }
            case FAIL: {
                pluginOperator.jobFailedCallback(pipelineRecord, jobRecord, message, data);
                break;
            }
            case ABORT: {
                pluginOperator.jobAbortCallback(pipelineRecord, jobRecord, message, data);
                break;
            }
            case CANCEL: {
                pluginOperator.jobCancelCallback(pipelineRecord, jobRecord, message);
                break;
            }
        }
    }


    private Plugin getPlugin(JobRecord jobRecord) {
        String jobType = jobRecord.getJobType();
        Plugin plugin = getPlugin(jobType);
        if (plugin == null) {
            throw new PluginNotExistException(jobType);
        }
        return plugin;
    }

    private void sendCharge(Pipeline pipeline, JobRecord jobRecord) {
        Date createTime = jobRecord.getCreateTime();
        if (createTime == null) {
            return;
        }
        Long companyId = pipeline.getCompanyId();
        Date finishedDate = new Date();
        if (pipeline.isUseDefaultCluster()) {
            resourceChargeProducer.sendCharge(companyId, createTime, finishedDate, BillingItem.CALCULATION_RESOURCE);
        } else {
            resourceChargeProducer.sendCharge(companyId, createTime, finishedDate, BillingItem.OWNER_RESOURCE);
        }
    }
}
