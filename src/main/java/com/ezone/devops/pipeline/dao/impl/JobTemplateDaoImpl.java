package com.ezone.devops.pipeline.dao.impl;

import com.ezone.devops.pipeline.dao.JobTemplateDao;
import com.ezone.devops.pipeline.model.JobTemplate;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JobTemplateDaoImpl extends BaseCommonDao<JobTemplate> implements JobTemplateDao {

    @Override
    public List<JobTemplate> getByTemplateId(Long templateId) {
        return find(match(TEMPLATE_ID, templateId));
    }

    @Override
    public boolean deleteByTemplateId(Long templateId) {
        return delete(match(TEMPLATE_ID, templateId)) > 0;
    }
}
