package com.ezone.devops.pipeline.dao;

import com.ezone.devops.pipeline.model.PipelineSetting;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface PipelineSettingDao extends LongKeyBaseDao<PipelineSetting> {

    String ID = "id";
    String COMPANY_ID = "company_id";
    String RECORD_EXPIRE_DAY = "record_expire_day";
    String REPORT_EXPIRE_DAY = "report_expire_day";

    PipelineSetting getByCompanyId(Long companyId);
}
