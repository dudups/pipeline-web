package com.ezone.devops.report.dao;

import com.ezone.devops.report.model.ReportInfo;
import com.ezone.galaxy.fasterdao.dao.LongKeyBaseDao;

public interface ReportInfoDao extends LongKeyBaseDao<ReportInfo> {

    String ID = "id";
    String COMPANY_ID = "company_id";
    String RUNNING = "running";
    String STORAGE_PATH = "storage_path";
    String REPORT_ID = "report_id";
    String PIPELINE_ID = "pipeline_id";

    ReportInfo getReportInfoByReportId(String reportId);
}
