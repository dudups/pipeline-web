package com.ezone.devops.pipeline.dao;

import com.ezone.devops.pipeline.model.Job;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

import java.util.Collection;
import java.util.List;

public interface JobDao extends LongKeyBaseDao<Job> {

    String ID = "id";
    String NAME = "name";
    String CONDITION_TYPE = "condition_type";
    String CONDITION_TRIGGER_TYPE = "condition_trigger_type";
    String JOB_TYPE = "job_type";
    String PLUGIN_TYPE = "plugin_type";
    String STAGE_ID = "stage_id";
    String PLUGIN_ID = "plugin_id";
    String UPSTREAM_ID = "upstream_id";

    List<Job> getJobsByStageId(Long stageId);

    List<Job> getJobsByStageIds(Collection<Long> stageIds);

    List<Job> getByJobType(String jobType);

    void deleteAll();

    int deleteNotExistByStageIds(Collection<Long> stageIds);
}
