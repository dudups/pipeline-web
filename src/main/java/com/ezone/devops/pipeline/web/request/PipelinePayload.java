package com.ezone.devops.pipeline.web.request;

import com.ezone.devops.pipeline.enums.ResourceType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Data
public class PipelinePayload {

    @NotBlank(message = "流水线名称不允许为空")
    @Length(min = 1, max = 100, message = "流水线名称长度在1到100个字符之间")
    private String name;
    @NotBlank(message = "代码库名称不允许为空")
    @Length(min = 1, max = 120, message = "代码库名称长度在1到200个字符之间")
    private String repoName;
    // 每个job任务超时配置
    @Min(value = 1, message = "超时时间最短不得少于1分钟")
    @Max(value = 720, message = "超时时间最长不得超过12小时")
    private int jobTimeoutMinute;
    private boolean matchAllBranch;
    // 匹配规则
    @Valid
    private List<BranchPatternConfigPayload> branchPatternConfig;
    @Valid
    @NotNull(message = "触发方式不能为空")
    private TriggerConfigPayload pipelineTriggerConfig;
    // 用户自定义环境变量
    @Valid
    private List<EnvConfigPayload> envConfig;
    // 流水线事件的通知配置
    @Valid
    private List<NoticeConfigPayload> noticeConfig;
    @Valid
    @NotEmpty(message = "阶段不能为空")
    private List<StagePayload> stageConfig;
    private ResourceType resourceType;
    @Length(max = 100, message = "构建资源名称长度不能超过100")
    private String resourceName;
    private String clusterName = StringUtils.EMPTY;
    private boolean useDefaultCluster = false;

    private boolean inheritRepoPermission;

    @JsonIgnore
    public List<JobPayload> getAllReleaseJob() {
        List<JobPayload> releaseJobs = Lists.newArrayList();
        for (StagePayload stagePayload : stageConfig) {
            List<JobPayload> allBuildJob = stagePayload.getAllReleaseJob();
            releaseJobs.addAll(allBuildJob);
        }
        return releaseJobs;
    }

}
