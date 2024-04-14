package com.ezone.devops.pipeline.dao;

import com.ezone.devops.pipeline.model.JobTemplate;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

import java.util.List;

public interface JobTemplateDao extends LongKeyBaseDao<JobTemplate> {

    String ID = "id";
    String TEMPLATE_ID = "template_id";
    String JOB_NAME = "job_name";
    String AUTO_BUILD = "auto_build";
    String JOB_TYPE = "job_type";
    String PLUGIN_TYPE = "plugin_type";
    String STAGE_ID = "stage_id";
    String UPSTREAM_ID = "upstream_id";

    List<JobTemplate> getByTemplateId(Long templateId);

    boolean deleteByTemplateId(Long templateId);
}
