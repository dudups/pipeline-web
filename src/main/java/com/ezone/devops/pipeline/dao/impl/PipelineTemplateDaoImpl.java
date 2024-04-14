package com.ezone.devops.pipeline.dao.impl;

import com.ezone.devops.pipeline.dao.PipelineTemplateDao;
import com.ezone.devops.pipeline.model.PipelineTemplate;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import com.ezone.galaxy.fasterdao.operator.Match;
import com.ezone.galaxy.fasterdao.operator.QueryBuilder;
import com.ezone.galaxy.fasterdao.page.Page;
import com.ezone.galaxy.framework.common.bean.PageResult;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PipelineTemplateDaoImpl extends BaseCommonDao<PipelineTemplate> implements PipelineTemplateDao {

    @Override
    public PipelineTemplate getByName(Long companyId, String name) {
        return findOne(match(COMPANY_ID, companyId), match(NAME, name));
    }

    @Override
    public PageResult<List<PipelineTemplate>> suggestByName(Long companyId, String name, int pageNumber, int pageSize) {
        List<Match> matches = Lists.newArrayList();
        matches.add(match(COMPANY_ID, companyId));

        if (StringUtils.isNotBlank(name)) {
            matches.add(match(NAME, like(name)));
        }

        int total = count(matches);
        QueryBuilder queryBuilder = new QueryBuilder();
        Page page = new Page(pageNumber, pageSize, total);
        queryBuilder.page(page);
        queryBuilder.addMatch(matches);
        queryBuilder.addOrder(order(MODIFY_TIME, false));

        List<PipelineTemplate> configMapConfigs = executeSelect(queryBuilder);
        return new PageResult<>(total, configMapConfigs);
    }
}
