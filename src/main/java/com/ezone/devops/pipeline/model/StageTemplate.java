package com.ezone.devops.pipeline.model;

import com.ezone.devops.pipeline.dao.StageTemplateDao;
import com.ezone.devops.pipeline.web.request.StageTemplatePayload;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "stage_template")
public class StageTemplate extends LongID {

    @Column(StageTemplateDao.ID)
    private Long id;
    @Column(StageTemplateDao.TEMPLATE_ID)
    private Long templateId;
    @Column(StageTemplateDao.STAGE_NAME)
    private String stageName;
    @Column(StageTemplateDao.UPSTREAM_ID)
    private Long upstreamId;

    public StageTemplate(PipelineTemplate pipelineTemplate, StageTemplatePayload payload, Long upstreamId) {
        setTemplateId(pipelineTemplate.getId());
        setStageName(payload.getStageName());
        setUpstreamId(upstreamId);
    }
}
