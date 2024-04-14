package com.ezone.devops.pipeline.dao;

import com.ezone.devops.pipeline.model.Stage;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

import java.util.Collection;
import java.util.List;

public interface StageDao extends LongKeyBaseDao<Stage> {

    String ID = "id";
    String PIPELINE_ID = "pipeline_id";
    String UPSTREAM_ID = "upstream_id";
    String NAME = "name";

    List<Stage> getByPipelineId(Long pipelineId);

    List<Stage> getByPipelineIds(Collection<Long> pipelineIds);

    int deleteNotExistByPipelineIds(Collection<Long> pipelineIds);

    void deleteAll();

}
