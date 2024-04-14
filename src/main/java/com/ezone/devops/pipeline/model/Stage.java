package com.ezone.devops.pipeline.model;

import com.ezone.devops.pipeline.dao.StageDao;
import com.ezone.devops.pipeline.web.request.StagePayload;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "stage")
public class Stage extends LongID {

    @Column(StageDao.ID)
    private Long id;
    @Column(StageDao.UPSTREAM_ID)
    private Long upstreamId;
    @Column(StageDao.PIPELINE_ID)
    private Long pipelineId;
    @Column(StageDao.NAME)
    private String name;

    public Stage(Pipeline pipeline, StagePayload stagePayload, Long upstreamId) {
        setPipelineId(pipeline.getId());
        setUpstreamId(upstreamId);
        setName(stagePayload.getStageName());
    }
}
