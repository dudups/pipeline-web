package com.ezone.devops.pipeline.model;

import com.ezone.devops.pipeline.dao.NoticeDao;
import com.ezone.devops.pipeline.enums.MemberType;
import com.ezone.devops.pipeline.web.request.NoticeConfigPayload;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "notice")
public class Notice extends LongID {

    @Column(NoticeDao.ID)
    private Long id;
    @Column(NoticeDao.PIPELINE_ID)
    private Long pipelineId;
    @Column(NoticeDao.MEMBER_TYPE)
    private MemberType memberType;
    @Column(NoticeDao.NAME)
    private String name;
    @Column(NoticeDao.START)
    private boolean start;
    @Column(NoticeDao.SUCCESS)
    private boolean success;
    @Column(NoticeDao.FAILED)
    private boolean failed;

    public Notice(Pipeline pipeline, NoticeConfigPayload payload) {
        this.pipelineId = pipeline.getId();
        this.memberType = payload.getMemberType();
        this.name = payload.getName();
        this.start = payload.isStart();
        this.failed = payload.isFailed();
        this.success = payload.isSuccess();
    }
}
