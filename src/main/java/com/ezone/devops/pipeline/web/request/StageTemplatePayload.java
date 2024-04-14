package com.ezone.devops.pipeline.web.request;

import com.ezone.devops.plugins.enums.PluginType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class StageTemplatePayload {

    @NotBlank(message = "模板名称不得为空")
    @Length(min = 1, max = 32, message = "模板名称不得超过32个字")
    private String stageName;
    @Valid
    private List<List<JobTemplatePayload>> groupJobs;

    @JsonIgnore
    public List<JobTemplatePayload> getAllReleaseJob() {
        List<JobTemplatePayload> releaseJobs = Lists.newArrayList();
        for (List<JobTemplatePayload> groupJob : groupJobs) {
            if (CollectionUtils.isEmpty(groupJob)) {
                continue;
            }
            List<JobTemplatePayload> compileJobs = groupJob.stream()
                    .filter(payload -> payload.getPluginType() == PluginType.RELEASE)
                    .collect(Collectors.toList());
            releaseJobs.addAll(compileJobs);
        }

        return releaseJobs;
    }

}
