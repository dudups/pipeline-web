package com.ezone.devops.pipeline.clients;

import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.util.HttpClient;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "system.log")
public class CiLogClient {

    private static final String DATA_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat sdf = new SimpleDateFormat(DATA_FORMAT);
    private static final String LOG_FORMAT = "[%s] [%s] %s";
    private static final String NONE_COLOR = "\033[0;m";
    private static final String ERROR_COLOR = "\033[31;1m";
    private static final String INFO_COLOR = "\033[32;1m";
    private static final String WARNING_COLOR = "\033[0;33m";

    private final static String WRITE_LOG_URL = "/internal/logs/%s/write";
    private final static String LOG_STREAM_URL = "/internal/logs/%s/stream";

    private int timeout = 15000;
    private String endpoint;
    private String applicationName = "ezpipeline";

    public BaseResponse<?> sendLnLog(String logName, LogLevel logLevel, String logContent) {
        if (StringUtils.isBlank(logName) || StringUtils.isBlank(logContent)) {
            return null;
        }

        String formatLog = formatLog(logContent, logLevel);

        Map<String, String> params = Maps.newHashMap();
        params.put("logContent", formatLog);

        return new HttpClient(getEndpoint()).path(String.format(WRITE_LOG_URL, logName))
                .jsonBody(params).post(BaseResponse.class);
    }

    public InputStream readLogStream(String logName, boolean download) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        StringBuffer url = new StringBuffer();
        String path = String.format(LOG_STREAM_URL, logName);
        url.append(getEndpoint())
                .append(path)
                .append("?")
                .append("download=").append(download);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .setSocketTimeout(timeout)
                .setRedirectsEnabled(true).build();
        HttpGet httpGet = new HttpGet(url.toString());
        httpGet.setConfig(requestConfig);
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null) {
                return httpEntity.getContent();
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("request ci log error", e);
        }
        return null;
    }

    private static String formatLog(String log, LogLevel logLevel) {
        if (StringUtils.isEmpty(log)) {
            return log;
        }
        String colorLog;
        String lineSeparator = System.lineSeparator();
        switch (logLevel) {
            case INFO: {
                colorLog = INFO_COLOR + format(log, logLevel) + NONE_COLOR + lineSeparator;
                break;
            }
            case WARN: {
                colorLog = WARNING_COLOR + format(log, logLevel) + NONE_COLOR + lineSeparator;
                break;
            }
            case ERROR: {
                colorLog = ERROR_COLOR + format(log, logLevel) + NONE_COLOR + lineSeparator;
                break;
            }
            case NONE:
            default: {
                colorLog = log;
            }
        }
        return colorLog;
    }

    private static String format(String log, LogLevel logLevel) {
        String date = sdf.format(new Date());
        return String.format(LOG_FORMAT, date, logLevel, log);
    }

    public enum LogLevel {
        NONE,
        INFO,
        WARN,
        ERROR;
    }
}
