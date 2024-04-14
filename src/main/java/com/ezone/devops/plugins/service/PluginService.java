package com.ezone.devops.plugins.service;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.model.Job;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.PipelineRecord;
import com.ezone.devops.plugins.job.Plugin;
import com.ezone.devops.plugins.job.PluginInfo;
import com.ezone.devops.plugins.web.request.PluginCallbackPayload;

import java.util.List;

public interface PluginService {

    Plugin getPlugin(String jobType);

    List<PluginInfo> listPlugins();

    Long savePluginConfig(Job job, JSONObject json);

    JSONObject getPluginConfig(Job job);

    JSONObject getPluginBuild(JobRecord jobRecord);

    boolean updatePluginBuild(JobRecord jobRecord, JSONObject updateFields);

    boolean cancelPluginBuild(PipelineRecord pipelineRecord, JobRecord jobRecord, String triggerUser);

    boolean handlePluginBuildCallback(Long pipelineId, Long jobId, PluginCallbackPayload payload);

}