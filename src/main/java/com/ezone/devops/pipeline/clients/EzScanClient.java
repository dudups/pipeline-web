package com.ezone.devops.pipeline.clients;

import com.ezone.devops.pipeline.clients.request.EzScanCancelPayload;
import com.ezone.devops.pipeline.clients.request.EzScanPayload;
import com.ezone.devops.pipeline.clients.response.EzScanResponse;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.util.HttpClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "system.ezscan")
public class EzScanClient {

    private static final String START_UTL = "/internal/scan/ezpipe";
    private static final String CANCEL_UTL = "/internal/task/%s/cancel";

    private static final String QUERY_LOG_URL = "/internal/task/%s/log";

    private String endpoint;
    private String platformName = "ezScan";

    public EzScanResponse triggerScan(EzScanPayload payload) {
        log.info("start invoke ezscan api, params:[{}]", payload);
        EzScanResponse response = new HttpClient(getEndpoint()).path(START_UTL).jsonBody(payload).retry(3).post(EzScanResponse.class);
        log.info("finished invoke ezscan api,params:[{}]", payload);
        return response;
    }

    public EzScanResponse cancelScan(Long taskId, EzScanCancelPayload payload) {
        log.info("start invoke ezscan cancel api, params:[{}]", payload);
        String path = String.format(CANCEL_UTL, taskId);
        EzScanResponse response = new HttpClient(getEndpoint()).path(path).jsonBody(payload).retry(3).post(EzScanResponse.class);
        log.info("finished invoke ezscan cancel api,params:[{}]", payload);
        return response;
    }

    public BaseResponse<?> queryScanLog(Long taskId, Long projectId) {
        log.info("start invoke ezscan query log api, taskId:[{}] projectId:[{}]", taskId, projectId);
        String path = String.format(QUERY_LOG_URL, taskId);
        BaseResponse<?> response = new HttpClient(getEndpoint()).path(path).param("projectId", String.valueOf(projectId)).retry(3).get(BaseResponse.class);
        log.info("finished invoke ezscan query log api, taskId:[{}] projectId:[{}]", taskId, projectId);
        return response;
    }

}
