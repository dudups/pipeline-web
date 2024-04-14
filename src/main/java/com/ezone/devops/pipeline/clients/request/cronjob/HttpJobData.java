package com.ezone.devops.pipeline.clients.request.cronjob;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpJobData {

    @ApiModelProperty("回调请求的http方法")
    private HttpMethod method;
    @ApiModelProperty("回调的url")
    private String url;
    @ApiModelProperty("回调时候封装到query param里")
    private Map<String, String> params;
    @ApiModelProperty("回调时候封装到请求头里")
    private Map<String, String> headers;
    @ApiModelProperty("回调时候封装到请求体里")
    private Map<String, String> body;
}
