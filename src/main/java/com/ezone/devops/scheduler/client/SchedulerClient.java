package com.ezone.devops.scheduler.client;

import com.ezone.devops.pipeline.enums.ResourceType;
import com.ezone.devops.scheduler.client.response.RunnerJobResponse;
import com.ezone.devops.scheduler.job.JenkinsJob;
import com.ezone.devops.scheduler.job.RunnerJob;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.util.HttpClient;
import com.google.common.collect.Maps;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "system.resource-manager")
public class SchedulerClient {

    private final static String QUERY_PERMISSION_URL = "/internal/resources/permissions";
    private final static String RUNNER_JOB_URL = "/internal/runner/jobs";
    private final static String CANCEL_RUNNER_JOB_URL = "/internal/runner/jobs/%s";

    private final static String JENKINS_JOB_URL = "/internal/jenkins/jobs";
    private final static String CANCEL_JENKINS_JOB_URL = "/internal/jenkins/jobs/%s";

    private String endpoint;

    /**
     * 查询用户对资源是否有使用权限
     *
     * @param companyId
     * @param resourceType
     * @param resourceName
     * @param username
     * @return
     */
    public BaseResponse<?> queryPermission(Long companyId, ResourceType resourceType,
                                           String resourceName, String username) {
        Map<String, String> params = Maps.newHashMap();
        params.put("companyId", String.valueOf(companyId));
        params.put("resourceType", String.valueOf(resourceType));
        params.put("resourceName", resourceName);
        params.put("username", username);
        return new HttpClient(getEndpoint()).path(QUERY_PERMISSION_URL).param(params).get(BaseResponse.class);
    }

    /**
     * 创建job
     *
     * @param payload
     * @return
     */
    public RunnerJobResponse createRunnerJob(RunnerJob payload) {
        if (payload == null) {
            return null;
        }

        return new HttpClient(getEndpoint()).path(RUNNER_JOB_URL).jsonBody(payload).post(RunnerJobResponse.class);
    }

    public BaseResponse<?> cancelRunnerJob(Long jobId) {
        if (jobId == null) {
            return null;
        }

        return new HttpClient(getEndpoint()).path(String.format(CANCEL_RUNNER_JOB_URL, jobId))
                .delete(BaseResponse.class);
    }

    public RunnerJobResponse createJenkinsJob(JenkinsJob jenkinsJob) {
        if (jenkinsJob == null) {
            return null;
        }

        return new HttpClient(getEndpoint()).path(JENKINS_JOB_URL).jsonBody(jenkinsJob).post(RunnerJobResponse.class);
    }
}
