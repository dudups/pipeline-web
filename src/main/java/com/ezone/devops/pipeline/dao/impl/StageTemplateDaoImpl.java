package com.ezone.devops.pipeline.dao.impl;

import com.ezone.devops.pipeline.dao.StageTemplateDao;
import com.ezone.devops.pipeline.model.StageTemplate;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class StageTemplateDaoImpl extends BaseCommonDao<StageTemplate> implements StageTemplateDao {

    @Override
    public List<StageTemplate> getByTemplateId(Long templateId) {
        return find(match(TEMPLATE_ID, templateId), order(UPSTREAM_ID, true));
    }

    @Override
    public List<StageTemplate> getByTemplateIds(Collection<Long> templateIds) {
        return find(match(TEMPLATE_ID, templateIds), order(UPSTREAM_ID, true));
    }

    @Override
    public boolean deleteByTemplateId(Long templateId) {
        return delete(match(TEMPLATE_ID, templateId)) > 0;
    }
}
