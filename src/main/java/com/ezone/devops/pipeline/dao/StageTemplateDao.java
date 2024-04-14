package com.ezone.devops.pipeline.dao;

import com.ezone.devops.pipeline.model.StageTemplate;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

import java.util.Collection;
import java.util.List;

public interface StageTemplateDao extends LongKeyBaseDao<StageTemplate> {

    String ID = "id";
    String TEMPLATE_ID = "template_id";
    String STAGE_NAME = "stage_name";
    String UPSTREAM_ID = "upstream_id";

    List<StageTemplate> getByTemplateId(Long templateId);

    List<StageTemplate> getByTemplateIds(Collection<Long> templateIds);

    boolean deleteByTemplateId(Long templateId);

}
