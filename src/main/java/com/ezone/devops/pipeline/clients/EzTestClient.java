package com.ezone.devops.pipeline.clients;

import com.ezone.devops.pipeline.clients.request.EzTestPayload;
import com.ezone.devops.pipeline.clients.response.EzTestResponse;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.plugins.job.test.eztest.api.model.EzTestConfig;
import com.ezone.devops.plugins.job.test.eztest.performance.bean.EzTestPerformanceConfigBean;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.util.HttpClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "system.eztest")
public class EzTestClient {

    private static final String START_API_URL = "/api/internal/plan/apiTestPlan/ci/start";
    private static final String STOP_API_URL = "/api/internal/plan/apiTestPlan/ci/stop";
    private static final String START_PERFORMANCE_URL = "/api/internal/plan/performanceTestPlan/ci/start";
    private static final String STOP_PERFORMANCE_URL = "/api/internal/plan/performanceTestPlan/ci/stop";
    private static final String AUTH_MD5_HEADER = "X-INTERNAL-AUTH-MD5";
    private static final String AUTH_TIMESTAMP_HEADER = "X-INTERNAL-AUTH-TIMESTAMP";

    private String token;
    private String endpoint;
    private String platformName = "ezTest";

    public EzTestResponse startApiTest(Pipeline pipeline, JobRecord jobRecord, EzTestConfig ezTestConfig, Map<String, String> envs) {
        EzTestPayload payload = new EzTestPayload(pipeline, jobRecord, ezTestConfig, envs);
        log.info("start invoke eztest api, params:[{}]", payload);
        EzTestResponse response = new HttpClient(getEndpoint()).path(START_API_URL).header(getHeaders()).jsonBody(payload).retry(3).post(EzTestResponse.class, false).getData();
        log.info("finished invoke eztest api,params:[{}]", payload);
        return response;
    }

    public BaseResponse<?> stopApiTest(Long jobId) {
        return new HttpClient(getEndpoint()).path(STOP_API_URL).header(getHeaders()).param("jobId", String.valueOf(jobId)).retry(3).post(BaseResponse.class);
    }

    public EzTestResponse startPerformanceTest(Pipeline pipeline, JobRecord jobRecord, EzTestPerformanceConfigBean ezTestPerformanceConfigBean, Map<String, String> envs) {
        EzTestPayload payload = new EzTestPayload(pipeline, jobRecord, ezTestPerformanceConfigBean, envs);
        log.info("start invoke eztest performance, params:[{}]", payload);
        EzTestResponse response = new HttpClient(getEndpoint()).path(START_PERFORMANCE_URL).header(getHeaders()).jsonBody(payload).retry(3).post(EzTestResponse.class, false).getData();
        log.info("finished invoke eztest performance,params:[{}]", payload);
        return response;
    }

    public BaseResponse<?> stopPerformanceTest(Long jobId) {
        return new HttpClient(getEndpoint()).path(STOP_PERFORMANCE_URL).header(getHeaders()).param("jobId", String.valueOf(jobId)).retry(3).post(BaseResponse.class);
    }

    private Map<String, String> getHeaders() {
        long timeMillis = System.currentTimeMillis();
        String temp = getToken() + timeMillis;
        String md5Hex = DigestUtils.md5Hex(temp);
        Map<String, String> headers = new HashMap<>();
        headers.put(AUTH_MD5_HEADER, md5Hex);
        headers.put(AUTH_TIMESTAMP_HEADER, String.valueOf(timeMillis));
        return headers;
    }
}
