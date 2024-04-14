package com.ezone.devops.pipeline.model;

import com.ezone.devops.pipeline.dao.PipelineSettingDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "pipeline_setting")
public class PipelineSetting extends LongID {

    @JsonIgnore
    @Column(PipelineSettingDao.ID)
    private Long id;
    @JsonIgnore
    @Column(PipelineSettingDao.COMPANY_ID)
    private Long companyId;
    @Column(PipelineSettingDao.RECORD_EXPIRE_DAY)
    private int recordExpireDay;
    @Column(PipelineSettingDao.REPORT_EXPIRE_DAY)
    private int reportExpireDay;
}
