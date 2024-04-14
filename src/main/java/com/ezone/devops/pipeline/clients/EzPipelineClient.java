package com.ezone.devops.pipeline.clients;

import com.ezone.devops.pipeline.clients.response.PipelineBuildResponse;
import com.ezone.devops.pipeline.common.ScmTriggerType;
import com.ezone.devops.pipeline.common.TriggerMode;
import com.ezone.devops.pipeline.model.JobRecord;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.web.request.InternalTriggerPipelinePayload;
import com.ezone.galaxy.framework.common.util.HttpClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "system.pipeline")
public class EzPipelineClient {

    private static final String TRIGGER_PIPELINE_URL = "/internal/pipelines/%s";

    private static final String PIPELINE_CALLBACK_PATH = "%s/internal/triggers/crontab/%s";
    private static final String PIPELINE_JOB_CALLBACK_PATH = "%s/internal/pipelines/%s/jobs/%s";


    private String endpoint;
    private String applicationName = "ezpipeline";

    public PipelineBuildResponse triggerPipeline(Long pipelineId, String branchName, String triggerUser,
                                                 TriggerMode triggerMode, String callbackUrl) {
        InternalTriggerPipelinePayload payload = new InternalTriggerPipelinePayload();
        payload.setScmTriggerType(ScmTriggerType.BRANCH);
        payload.setExternalName(branchName);
        payload.setTriggerUser(triggerUser);
        payload.setCallbackUrl(callbackUrl);
        payload.setTriggerMode(triggerMode);
        return new HttpClient(getEndpoint()).path(String.format(TRIGGER_PIPELINE_URL, pipelineId))
                .jsonBody(payload).retry(3).post(PipelineBuildResponse.class);
    }

    public String getTriggerPipelineCallbackUrl(Pipeline pipeline) {
        return String.format(PIPELINE_CALLBACK_PATH, getEndpoint(), pipeline.getId());
    }

    public String getJobCallbackUrl(JobRecord jobRecord) {
        return String.format(PIPELINE_JOB_CALLBACK_PATH, getEndpoint(), jobRecord.getPipelineId(), jobRecord.getExternalJobId());
    }
}
