package com.ezone.devops.pipeline.model;

import com.ezone.devops.pipeline.common.BuildStatus;
import com.ezone.devops.pipeline.dao.StageRecordDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Table(name = "stage_record")
public class StageRecord extends LongID {

    public static final Long HEAD_STAGE_ID = 0L;

    @Column(StageRecordDao.ID)
    private Long id;
    @Column(value = StageRecordDao.PIPELINE_ID, maybeModified = false)
    private Long pipelineId;
    @Column(value = StageRecordDao.PIPELINE_RECORD_ID, maybeModified = false)
    private Long pipelineRecordId;
    @Column(StageRecordDao.NAME)
    private String name;
    @Column(StageRecordDao.STATUS)
    private BuildStatus status;
    @Column(StageRecordDao.UPSTREAM_ID)
    private Long upstreamId;
    @Column(StageRecordDao.CREATE_TIME)
    private Date createTime;
    @Column(StageRecordDao.MODIFY_TIME)
    private Date modifyTime;

    public StageRecord(PipelineRecord pipelineRecord, Stage stage, Long upstreamId) {
        setStatus(BuildStatus.PENDING);
        setName(stage.getName());
        setPipelineId(pipelineRecord.getPipelineId());
        setPipelineRecordId(pipelineRecord.getId());
        setUpstreamId(upstreamId);
        setCreateTime(new Date());
    }
}
