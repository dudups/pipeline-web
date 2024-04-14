package com.ezone.devops.report.model;

import com.ezone.devops.report.dao.ReportInfoDao;
import com.ezone.galaxy.fasterdao.annotation.Column;
import com.ezone.galaxy.fasterdao.annotation.Table;
import com.ezone.galaxy.fasterdao.entity.LongID;
import lombok.Data;

@Data
@Table(name = "report_info")
public class ReportInfo extends LongID {

    @Column(ReportInfoDao.ID)
    private Long id;
    @Column(ReportInfoDao.COMPANY_ID)
    private Long companyId;
    @Column(ReportInfoDao.RUNNING)
    private boolean running;
    @Column(ReportInfoDao.STORAGE_PATH)
    private String storagePath;
    @Column(ReportInfoDao.REPORT_ID)
    private String reportId;
    @Column(ReportInfoDao.PIPELINE_ID)
    private Long pipelineId;

}
