package com.ezone.devops.pipeline.dao.impl;

import com.ezone.devops.pipeline.dao.PipelineSettingDao;
import com.ezone.devops.pipeline.model.PipelineSetting;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class PipelineSettingDaoImpl extends BaseCommonDao<PipelineSetting> implements PipelineSettingDao {

    @Override
    public PipelineSetting getByCompanyId(Long companyId) {
        return findOne(match(COMPANY_ID, companyId));
    }

}
