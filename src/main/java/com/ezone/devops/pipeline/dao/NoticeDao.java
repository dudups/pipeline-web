package com.ezone.devops.pipeline.dao;

import com.ezone.devops.pipeline.model.Notice;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

import java.util.Collection;
import java.util.List;

public interface NoticeDao extends LongKeyBaseDao<Notice> {

    String ID = "id";
    String PIPELINE_ID = "pipeline_id";
    String MEMBER_TYPE = "member_type";
    String NAME = "name";
    String START = "start";
    String SUCCESS = "success";
    String FAILED = "failed";

    List<Notice> getNoticeConfigByPipelineId(Long pipelineId);

    boolean deleteByPipelineId(Long pipelineId);

    void deleteAll();

    int deleteNotExistByPipelineIds(Collection<Long> pipelineIds);

}
