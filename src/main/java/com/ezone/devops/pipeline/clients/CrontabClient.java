package com.ezone.devops.pipeline.clients;

import com.ezone.devops.pipeline.clients.request.cronjob.*;
import com.ezone.devops.pipeline.config.SystemConfig;
import com.ezone.devops.pipeline.enums.CrontabTriggerType;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.web.request.CrontabPayload;
import com.ezone.galaxy.framework.common.bean.BaseResponse;
import com.ezone.galaxy.framework.common.util.HttpClient;
import com.ezone.galaxy.framework.main.concurrent.aop.Pooled;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "system.crontab")
public class CrontabClient {

    private static final String SYSTEM_TYPE = "EZPIPELINE";
    private static final String PIPELINE_TYPE = "pipeline-job";
    private static final String JOB_TIMEOUT_TYPE = "timeout-job";
    private static final String DAILY_PATH = "/crontab/http/daily";
    private static final String FIXED_PATH = "/crontab/http/fixed";
    private static final String TIMEOUT_PATH = "/crontab/http/once";
    private static final String DELETE_PATH = "/crontab/http";
    private static final String DEPRECATED_DELETE_PATH = "/crontab";

    private String endpoint;

    @Autowired
    private SystemConfig systemConfig;
    @Autowired
    private EzPipelineClient ezPipelineClient;

    public BaseResponse<?> registerCrontab(Pipeline pipeline, CrontabPayload crontabPayload) {
        if (crontabPayload == null) {
            return null;
        }
        String jobName = getPipelineJobName(pipeline);
        deleteCrontab(jobName);
        String callbackUrl = ezPipelineClient.getTriggerPipelineCallbackUrl(pipeline);
        if (crontabPayload.getType() == CrontabTriggerType.FIXED) {
            return registerFixedCrontab(jobName, callbackUrl, crontabPayload);
        }
        return registerDailyCrontab(jobName, callbackUrl, crontabPayload);
    }

    private BaseResponse<?> registerDailyCrontab(String jobName, String callbackUrl, CrontabPayload crontabPayload) {
        DailyTrigger dailyTrigger = new DailyTrigger();
        dailyTrigger.setFrom(crontabPayload.getFrom());
        dailyTrigger.setTo(crontabPayload.getTo());
        dailyTrigger.setDayOfWeek(getDayOfWeeks(crontabPayload.getDayOfWeek()));
        dailyTrigger.setInterval(crontabPayload.getInterval());

        DailyHttpCron dailyHttpCron = new DailyHttpCron();
        dailyHttpCron.setJobIdentity(generateJobIdentity(jobName));
        dailyHttpCron.setTrigger(dailyTrigger);
        dailyHttpCron.setHttpJobData(generateHttpJobData(callbackUrl));

        return new HttpClient(getEndpoint()).path(DAILY_PATH).jsonBody(dailyHttpCron).retry(3).post(BaseResponse.class);
    }

    private BaseResponse<?> registerFixedCrontab(String jobName, String callbackUrl, CrontabPayload crontabPayload) {
        FixedTrigger fixedTrigger = new FixedTrigger();
        fixedTrigger.setFixedTime(crontabPayload.getFixedTime());
        fixedTrigger.setDayOfWeek(getDayOfWeeks(crontabPayload.getDayOfWeek()));

        FixedHttpCron fixedHttpCron = new FixedHttpCron();
        fixedHttpCron.setJobIdentity(generateJobIdentity(jobName));
        fixedHttpCron.setTrigger(fixedTrigger);
        fixedHttpCron.setHttpJobData(generateHttpJobData(callbackUrl));

        return new HttpClient(getEndpoint()).path(FIXED_PATH).jsonBody(fixedHttpCron).retry(3).post(BaseResponse.class);
    }

    private Set<Integer> getDayOfWeeks(String dayOfWeek) {
        String[] split = dayOfWeek.split(",");
        Set<Integer> result = new HashSet<>();
        for (String s : split) {
            result.add(Integer.parseInt(s));
        }

        return result;
    }

    private JobIdentity generateJobIdentity(String jobName) {
        JobIdentity jobIdentity = new JobIdentity();
        jobIdentity.setSystemType(SYSTEM_TYPE);
        jobIdentity.setJobName(jobName);
        return jobIdentity;
    }

    private HttpJobData generateHttpJobData(String callbackUrl) {
        HttpJobData httpJobData = new HttpJobData();
        httpJobData.setUrl(callbackUrl);
        httpJobData.setMethod(HttpMethod.POST);
        return httpJobData;
    }

    @Pooled(timeout = 20000)
    public BaseResponse<?> registerJobTimeout(JobRecord jobRecord, int futureMinute) {
        String callbackUrl = ezPipelineClient.getJobCallbackUrl(jobRecord);

        OnceTrigger onceTrigger = new OnceTrigger();
        onceTrigger.setExecuteTimestamp(DateUtils.addMinutes(new Date(), futureMinute).getTime());

        OnceHttpCron onceHttpCron = new OnceHttpCron();
        onceHttpCron.setJobIdentity(generateJobIdentity(getTimeoutJobName(jobRecord)));
        onceHttpCron.setTrigger(onceTrigger);
        onceHttpCron.setHttpJobData(generateHttpJobData(callbackUrl));

        return new HttpClient(getEndpoint()).path(TIMEOUT_PATH).jsonBody(onceHttpCron).retry(3).post(BaseResponse.class);
    }

    public BaseResponse<?> deletePipelineCrontab(Pipeline pipeline) {
        String jobName = getPipelineJobName(pipeline);
        return deleteCrontab(jobName);
    }

    @Pooled(timeout = 20000)
    public BaseResponse<?> deleteJobTimeout(JobRecord jobRecord) {
        return deleteCrontab(getTimeoutJobName(jobRecord));
    }

    private String getPipelineJobName(Pipeline pipeline) {
        return PIPELINE_TYPE + "-pipeline-" + pipeline.getId();
    }

    private String getTimeoutJobName(JobRecord jobRecord) {
        return JOB_TIMEOUT_TYPE + "-pipeline-" + jobRecord.getPipelineId() + "-job-" + jobRecord.getExternalJobId();
    }

    private BaseResponse<?> deleteCrontab(String jobName) {
        return new HttpClient(getEndpoint()).path(DELETE_PATH)
                .param("jobName", jobName).param("systemType", SYSTEM_TYPE)
                .retry(3).delete(BaseResponse.class);
    }

    @Deprecated
    public BaseResponse<?> deprecatedDeleteCrontab(Long pipelineId, String jobGroup) {
        return new HttpClient(getEndpoint()).path(DEPRECATED_DELETE_PATH).param("jobName", String.valueOf(pipelineId)).param("jobGroup", jobGroup).retry(3).delete(BaseResponse.class);
    }


}
