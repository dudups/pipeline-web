package com.ezone.devops.pipeline.dao;

import com.ezone.devops.pipeline.model.PipelineTemplate;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;
import com.ezone.galaxy.framework.common.bean.PageResult;

import java.util.List;

public interface PipelineTemplateDao extends LongKeyBaseDao<PipelineTemplate> {

    String ID = "id";
    String COMPANY_ID = "company_id";
    String NAME = "name";
    String CREATE_USER = "create_user";
    String CREATE_TIME = "create_time";
    String MODIFY_TIME = "modify_time";

    PipelineTemplate getByName(Long companyId, String name);

    PageResult<List<PipelineTemplate>> suggestByName(Long companyId, String name, int pageNumber, int pageSize);

}
