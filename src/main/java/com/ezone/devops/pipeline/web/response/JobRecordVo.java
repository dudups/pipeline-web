package com.ezone.devops.pipeline.web.response;

import com.alibaba.fastjson.JSONObject;
import com.ezone.devops.pipeline.common.BuildStatus;
import com.ezone.devops.pipeline.enums.ConditionTriggerType;
import com.ezone.devops.pipeline.enums.JobConditionType;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.plugins.enums.PluginType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobRecordVo {

    private Long id;
    private Long pipelineId;
    private Long pipelineBuildId;
    private JobConditionType conditionType;
    private ConditionTriggerType conditionTriggerType;
    private String jobName;
    private String jobType;
    private String logName;
    private PluginType pluginType;
    private BuildStatus status;
    private String triggerUser;
    private Date createTime;
    private Date modifyTime;
    private String message;
    private JSONObject realJobBuild;

    public JobRecordVo(JobRecord jobRecord, JSONObject realJobBuild) {
        setId(jobRecord.getId());
        setPipelineId(jobRecord.getPipelineId());
        setPipelineBuildId(jobRecord.getPipelineRecordId());
        setConditionType(jobRecord.getConditionType());
        setConditionTriggerType(jobRecord.getConditionTriggerType());
        setJobName(jobRecord.getName());
        setJobType(jobRecord.getJobType());
        setLogName(jobRecord.getLogName());
        setPluginType(jobRecord.getPluginType());
        setStatus(jobRecord.getStatus());
        setTriggerUser(jobRecord.getTriggerUser());
        setCreateTime(jobRecord.getCreateTime());
        setModifyTime(jobRecord.getModifyTime());
        setMessage(jobRecord.getMessage());
        setRealJobBuild(realJobBuild);
    }
}
