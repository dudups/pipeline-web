package com.ezone.devops.pipeline.web.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class WebHookNoticePayload {

    @NotBlank(message = "代码库名称不允许为空")
    private String repoName;
    private boolean allPipeline;
    private boolean start;
    private boolean success;
    private boolean failed;
    private Set<WebHookNoticeEventPayload> noticeEvents;

    @Data
    public static class WebHookNoticeEventPayload {
        @NotNull(message = "流水线不能为空")
        private Long pipelineId;
        private boolean start;
        private boolean success;
        private boolean failed;
    }
}
