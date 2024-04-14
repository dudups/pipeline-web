package com.ezone.devops.pipeline.dao.impl;

import com.ezone.devops.pipeline.dao.NoticeDao;
import com.ezone.devops.pipeline.model.Notice;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import com.ezone.galaxy.fasterdao.param.NotParam;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class NoticeDaoImpl extends BaseCommonDao<Notice> implements NoticeDao {

    @Override
    public List<Notice> getNoticeConfigByPipelineId(Long pipelineId) {
        return find(match(PIPELINE_ID, pipelineId));
    }

    @Override
    public boolean deleteByPipelineId(Long pipelineId) {
        return delete(match(PIPELINE_ID, pipelineId)) > 0;
    }

    @Override
    public void deleteAll() {
        delete();
    }

    @Override
    public int deleteNotExistByPipelineIds(Collection<Long> pipelineIds) {
        return delete(match(PIPELINE_ID, new NotParam(pipelineIds)));
    }
}
