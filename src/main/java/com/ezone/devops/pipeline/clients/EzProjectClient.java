package com.ezone.devops.pipeline.clients;

import com.ezone.devops.pipeline.clients.request.EzProjectPayload;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.util.HttpClient;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class EzProjectClient {

    @Value("${system.ezproject.endpoint}")
    private String endpoint;
    @Value("${system.ezproject.token}")
    private String token;

    private static final String URL = "/project/api/project/isPassPipeline";
    private static final String HEADER_TIMESTAMP = "X-INTERNAL-AUTH-TIMESTAMP";
    private static final String HEADER_MD5 = "X-INTERNAL-AUTH-MD5";

    public BaseResponse<String> isPassPipeline(EzProjectPayload ezProjectPayload) {
        Long timestamp = System.currentTimeMillis();
        BaseResponse<String> response = new HttpClient(endpoint)
                .path(URL)
                .header(HEADER_TIMESTAMP, String.valueOf(timestamp))
                .header(HEADER_MD5, md5(timestamp))
                .jsonBody(ezProjectPayload)
                .retry(2)
                .timeout(30000)
                .post(BaseResponse.class);
        return response;
    }

    private String md5(Long timestamp) {
        return DigestUtils.md5Hex(this.token + timestamp);
    }
}
