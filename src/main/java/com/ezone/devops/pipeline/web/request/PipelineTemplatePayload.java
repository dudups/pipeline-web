package com.ezone.devops.pipeline.web.request;

import com.ezone.devops.pipeline.model.JobTemplate;
import com.ezone.devops.pipeline.model.PipelineTemplate;
import com.ezone.devops.pipeline.model.StageTemplate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PipelineTemplatePayload {

    private Long id;
    @NotBlank(message = "模板名称不得为空")
    @Length(min = 1, max = 32, message = "模板名称不得超过32个字")
    private String name;
    @Valid
    @NotNull(message = "阶段不能为空")
    private List<StageTemplatePayload> stages;

    private String createUser;
    private Date modifyTime;


    public PipelineTemplatePayload(PipelineTemplate pipelineTemplate,
                                   List<StageTemplate> stageTemplates,
                                   Map<Long, List<List<JobTemplate>>> groups) {
        setId(pipelineTemplate.getId());
        setName(pipelineTemplate.getName());
        setCreateUser(pipelineTemplate.getCreateUser());
        setModifyTime(pipelineTemplate.getModifyTime());

        if (CollectionUtils.isNotEmpty(stageTemplates)) {
            List<StageTemplatePayload> stages = new ArrayList<>();
            for (StageTemplate stageTemplate : stageTemplates) {
                StageTemplatePayload stageTemplatePayload = new StageTemplatePayload();
                stages.add(stageTemplatePayload);
                stageTemplatePayload.setStageName(stageTemplate.getStageName());
                if (MapUtils.isNotEmpty(groups)) {
                    List<List<JobTemplate>> groupJobs = groups.get(stageTemplate.getId());
                    if (CollectionUtils.isNotEmpty(groupJobs)) {
                        List<List<JobTemplatePayload>> results = new ArrayList<>();
                        for (List<JobTemplate> groupJob : groupJobs) {
                            List<JobTemplatePayload> result = new ArrayList<>();
                            for (JobTemplate jobTemplate : groupJob) {
                                result.add(new JobTemplatePayload(jobTemplate));
                            }
                            results.add(result);
                        }
                        stageTemplatePayload.setGroupJobs(results);
                    }
                }
            }
            setStages(stages);
        }
    }

    @JsonIgnore
    public List<JobTemplatePayload> getAllReleaseJob() {
        List<JobTemplatePayload> releaseJobs = Lists.newArrayList();
        for (StageTemplatePayload stageConfigPayload : stages) {
            List<JobTemplatePayload> allBuildJob = stageConfigPayload.getAllReleaseJob();
            releaseJobs.addAll(allBuildJob);
        }
        return releaseJobs;
    }
}
