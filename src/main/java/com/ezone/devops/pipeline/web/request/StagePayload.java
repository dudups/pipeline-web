package com.ezone.devops.pipeline.web.request;

import com.ezone.devops.pipeline.enums.JobConditionType;
import com.ezone.devops.plugins.enums.PluginType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class StagePayload {

    @NotBlank(message = "阶段名称不允许为空")
    @Length(min = 1, max = 100, message = "阶段名称长度在1到100个字符之间")
    private String stageName;
    @Valid
    @NotEmpty(message = "任务不能为空")
    private List<GroupJob> groupJobs;

    @JsonIgnore
    public List<JobPayload> getAllReleaseJob() {
        List<JobPayload> releaseJobs = Lists.newArrayList();
        for (GroupJob groupJob : groupJobs) {
            if (CollectionUtils.isEmpty(groupJob.getJobs())) {
                continue;
            }
            List<JobPayload> compileJobs = groupJob.getJobs().stream().filter(payload -> payload.getPluginType() == PluginType.RELEASE).peek(payload -> payload.setConditionType(JobConditionType.MANUAL)).collect(Collectors.toList());
            releaseJobs.addAll(compileJobs);
        }

        return releaseJobs;
    }

}
