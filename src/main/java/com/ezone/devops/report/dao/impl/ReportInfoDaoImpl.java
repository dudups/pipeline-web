package com.ezone.devops.report.dao.impl;

import com.ezone.devops.report.dao.ReportInfoDao;
import com.ezone.devops.report.model.ReportInfo;
import com.ezone.galaxy.fasterdao.dao.BaseCommonDao;
import org.springframework.stereotype.Repository;

@Repository
public class ReportInfoDaoImpl extends BaseCommonDao<ReportInfo> implements ReportInfoDao {

    @Override
    public ReportInfo getReportInfoByReportId(String reportId) {
        return findOne(match(REPORT_ID, reportId));
    }
}
