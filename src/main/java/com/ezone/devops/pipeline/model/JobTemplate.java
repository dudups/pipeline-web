package com.ezone.devops.pipeline.model;

import com.ezone.devops.pipeline.dao.JobTemplateDao;
import com.ezone.devops.pipeline.web.request.JobTemplatePayload;
import com.ezone.devops.plugins.enums.PluginType;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "job_template")
@NoArgsConstructor
public class JobTemplate extends LongID {

    @Column(JobTemplateDao.ID)
    private Long id;
    @Column(JobTemplateDao.TEMPLATE_ID)
    private Long templateId;
    @Column(JobTemplateDao.STAGE_ID)
    private Long stageId;
    @Column(JobTemplateDao.JOB_NAME)
    private String jobName;
    @Column(JobTemplateDao.AUTO_BUILD)
    private boolean autoBuild;
    @Column(JobTemplateDao.JOB_TYPE)
    private String jobType;
    @Column(JobTemplateDao.PLUGIN_TYPE)
    private PluginType pluginType;

    @Column(JobTemplateDao.UPSTREAM_ID)
    private Long upstreamId;

    public JobTemplate(PipelineTemplate pipelineTemplate, StageTemplate stageTemplate, JobTemplatePayload payload, Long upstreamId) {
        setTemplateId(pipelineTemplate.getId());
        setStageId(stageTemplate.getId());
        setJobName(payload.getJobName());
        setAutoBuild(payload.isAutoBuild());
        setJobType(payload.getJobType());
        setPluginType(payload.getPluginType());
        setUpstreamId(upstreamId);
    }
}
