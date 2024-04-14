package com.ezone.devops.pipeline.clients.request;

import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.plugins.job.test.eztest.api.model.EzTestConfig;
import com.ezone.devops.plugins.job.test.eztest.performance.bean.EzTestPerformanceConfigBean;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class EzTestPayload {

    private Long jobId;
    private Long plConfId;
    private Long envId;
    private boolean internal;
    private String resourceName;
    private String resourceType;
    private Long suiteId;
    private String user;
    private Map<String, String> args;

    public EzTestPayload(Pipeline pipeline, JobRecord jobRecord, EzTestConfig ezTestConfig, Map<String, String> args) {
        setJobId(jobRecord.getExternalJobId());
        setPlConfId(pipeline.getId());
        setEnvId(ezTestConfig.getEnvId());
        setInternal(pipeline.isUseDefaultCluster());
        setResourceName(pipeline.getClusterName());
        setResourceType(pipeline.getResourceType().name());
        setSuiteId(ezTestConfig.getSuiteId());
        setUser(jobRecord.getTriggerUser());
        setArgs(args);
    }

    public EzTestPayload(Pipeline pipeline, JobRecord jobRecord, EzTestPerformanceConfigBean ezTestConfig, Map<String, String> args) {
        setJobId(jobRecord.getExternalJobId());
        setPlConfId(jobRecord.getPipelineId());
        setEnvId(ezTestConfig.getEnvId());
        setInternal(!ezTestConfig.isUseCustomCluster());

        if (ezTestConfig.isUseCustomCluster()) {
            setResourceName(ezTestConfig.getResourceName());
            setResourceType(ezTestConfig.getResourceType().name());
        } else {
            setResourceName(pipeline.getClusterName());
            setResourceType(pipeline.getResourceType().name());
        }

        setSuiteId(ezTestConfig.getSuiteId());
        setUser(jobRecord.getTriggerUser());
        setArgs(args);
    }
}
