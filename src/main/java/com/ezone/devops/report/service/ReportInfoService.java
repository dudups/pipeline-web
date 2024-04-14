package com.ezone.devops.report.service;

import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.report.model.ReportInfo;

public interface ReportInfoService {

    ReportInfo createReportInfo(Pipeline pipeline);

    ReportInfo getReportInfoIfPresent(String reportId);

}
